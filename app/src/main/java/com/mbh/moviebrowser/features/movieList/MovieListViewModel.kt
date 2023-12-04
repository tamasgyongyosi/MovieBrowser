package com.mbh.moviebrowser.features.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.mbh.moviebrowser.data.FavoritesRepository
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.store.MovieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieStore: MovieStore,
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    val movies = combine(movieStore.movies.cachedIn(viewModelScope), favoritesRepository.get()) { movies, favorites ->
        movies.map {
            it.copy(isFavorite = favorites.contains(it.id))
        }
    }

    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsMovie.value = movie
    }
}
