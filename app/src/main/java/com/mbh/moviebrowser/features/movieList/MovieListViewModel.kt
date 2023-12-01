package com.mbh.moviebrowser.features.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.mbh.moviebrowser.data.toExternal
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.store.MovieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieStore: MovieStore,
) : ViewModel() {

    val movies = movieStore.movies.cachedIn(viewModelScope)

    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsId.value = movie.id
    }
}
