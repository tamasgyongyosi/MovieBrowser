package com.mbh.moviebrowser.data

import android.util.Log
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
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val genreRepository: GenreRepository,
    private val remoteKeyDao: RemoteKeyDao
) : RemoteMediator<Int, LocalMovie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, LocalMovie>): MediatorResult {
        return try {
            Log.i("Mediator", loadType.name)
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey ?: if (state.firstItemOrNull() == null) {
                        1
                    } else {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                }
            }
            Log.i("Mediator", "page: $page")
            val response = movieApi.getTrendingMovies(
                page = page
            )

            val endOfPaginationReached = response.results.size < state.config.pageSize

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.results.map {
                    RemoteKey(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                movieDao.insertAll(response.results.toLocal(genreRepository.get(), page))
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

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, LocalMovie>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieDatabase.withTransaction { remoteKeyDao.remoteKeyByMovieId(id) }
            }
        }
    }
}