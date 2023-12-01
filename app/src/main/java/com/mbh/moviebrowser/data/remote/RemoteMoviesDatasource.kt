package com.mbh.moviebrowser.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mbh.moviebrowser.api.MoviesApi
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class RemoteMoviesDatasource @Inject constructor(
    private val moviesApi: MoviesApi
) : PagingSource<Int, RemoteMovie>() {

    override fun getRefreshKey(state: PagingState<Int, RemoteMovie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteMovie> {
        return try {
            val currentPage = params.key ?: 1
            val movies = moviesApi.getTrendingMovies(page = currentPage)
            LoadResult.Page(
                data = movies.results,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (movies.results.isEmpty()) null else movies.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}