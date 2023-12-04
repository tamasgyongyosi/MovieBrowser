package com.mbh.moviebrowser.features.movieDetails

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.data.FavoritesRepository
import com.mbh.moviebrowser.store.MovieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieStore: MovieStore,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val movie = movieStore.detailsMovie

    fun onFavoriteClicked(isFavorite: Boolean) {
        movieStore.detailsMovie.value?.let { movie ->
            if (isFavorite) {
                favoritesRepository.add(movie.id)
            } else {
                favoritesRepository.remove(movie.id)
            }
            movieStore.detailsMovie.value = movie.copy(isFavorite = isFavorite)
        }
    }
}
