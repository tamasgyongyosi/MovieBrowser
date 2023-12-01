package com.mbh.moviebrowser.data

import com.mbh.moviebrowser.api.MoviesApi
import com.mbh.moviebrowser.data.local.MovieDao
import com.mbh.moviebrowser.data.remote.Genre
import com.mbh.moviebrowser.data.remote.RemoteMovie
import com.mbh.moviebrowser.domain.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val moviesApi: MoviesApi
) {

    fun getMovies(): Flow<List<Movie>> = movieDao.observeAll().distinctUntilChanged().map { movies ->
        withContext(Dispatchers.Default) {
            movies.toExternal()
        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.Default) {
            var remoteMovies = emptyList<RemoteMovie>()
            var genres = emptyList<Genre>()
            joinAll(
                async { remoteMovies = moviesApi.getTrendingMovies().results },
                async { genres = moviesApi.getGenres().results }
            )
            movieDao.insertAll(remoteMovies.toLocal(genres))
        }
    }
}