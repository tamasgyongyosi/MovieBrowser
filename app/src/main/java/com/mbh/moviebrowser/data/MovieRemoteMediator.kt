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
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val genreRepository: GenreRepository
) : RemoteMediator<Int, LocalMovie>() {

    private var lastPage = 1;

    override suspend fun load(loadType: LoadType, state: PagingState<Int, LocalMovie>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastPage + 1
                    }
                }
            }

            val response = movieApi.getTrendingMovies(
                page = loadKey
            )

            lastPage = response.page

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                }

                movieDao.insertAll(response.results.toLocal(genreRepository.get()))
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}