package com.mbh.moviebrowser.features.movieList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.store.MovieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieStore: MovieStore,
) : ViewModel() {

    val movies = movieStore.movies

    init {
        viewModelScope.launch {
            try {
                movieStore.refresh()
            } catch (e: Exception) {
                Log.e("MovieListViewModel", e.message, e)
            }
        }
    }

    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsId.value = movie.id
    }
}
