package com.mbh.moviebrowser.store

import com.mbh.moviebrowser.data.MovieRepository
import com.mbh.moviebrowser.domain.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieStore @Inject constructor(
    movieRepository: MovieRepository
) {

    val movies = movieRepository.getMovies()
    val detailsMovie: MutableStateFlow<Movie?> = MutableStateFlow(null)
}
