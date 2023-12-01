package com.mbh.moviebrowser.features.movieDetails

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.store.MovieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieStore: MovieStore,
) : ViewModel() {

    val movie = movieStore.getMovie(movieStore.detailsId.value)

    fun onFavoriteClicked(isFavorite: Boolean) {
        if (isFavorite) {
            movieStore.addToFavorite(movieStore.detailsId.value)
        } else {
            movieStore.removeFromFavorite(movieStore.detailsId.value)
        }
    }
}
