package com.mbh.moviebrowser.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mbh.moviebrowser.api.MovieApi
import com.mbh.moviebrowser.data.local.LocalMovie
import com.mbh.moviebrowser.data.local.MovieDao
import com.mbh.moviebrowser.data.local.MovieDatabase
import com.mbh.moviebrowser.data.local.RemoteKey
import com.mbh.moviebrowser.data.local.RemoteKeyDao
import com.mbh.moviebrowser.data.remote.Genre
import com.mbh.moviebrowser.data.remote.RemoteMovie
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class, DelicateCoroutinesApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val genreRepository: GenreRepository,
    private val remoteKeyDao: RemoteKeyDao
) : RemoteMediator<Int, LocalMovie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, LocalMovie>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    getRemoteKeyForLastItem(state)?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = state.firstItemOrNull() != null)
                }
            }

            if (loadType == LoadType.REFRESH) {
                genreRepository.clearAll()
            }

            var remoteMovies = emptyList<RemoteMovie>()
            var genres = emptyList<Genre>()

            joinAll(
                GlobalScope.async {
                    remoteMovies = movieApi.getTrendingMovies(page = page).results
                },
                GlobalScope.async {
                    genres = genreRepository.genres.ifEmpty {
                        movieApi.getGenres().results.also {
                            genreRepository.addAll(it)
                        }
                    }
                }
            )

            val endOfPaginationReached = remoteMovies.size < state.config.pageSize

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = remoteMovies.map {
                    RemoteKey(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                movieDao.insertAll(remoteMovies.toLocal(genres))
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LocalMovie>): RemoteKey? {
        return state.lastItemOrNull()?.let { movie ->
            movieDatabase.withTransaction { remoteKeyDao.remoteKeyByMovieId(movie.id) }
        }
    }
}