package com.mbh.moviebrowser.data

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.mbh.moviebrowser.data.local.LocalMovie
import com.mbh.moviebrowser.data.local.MovieDao
import com.mbh.moviebrowser.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val pager: Pager<Int, LocalMovie>,
    private val movieDao: MovieDao
) {
    fun getMovies(): Flow<PagingData<Movie>> = pager.flow.map { it.map { it.toExternal() } }
    fun getMovie(id: Long): Flow<Movie> = movieDao.getById(id).map {
        it.toExternal()
    }
}