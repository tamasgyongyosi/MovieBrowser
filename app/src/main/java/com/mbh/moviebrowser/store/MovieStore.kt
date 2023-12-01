package com.mbh.moviebrowser.store

import com.mbh.moviebrowser.data.FavoritesRepository
import com.mbh.moviebrowser.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieStore @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository
) {

    val movies = combine(movieRepository.getMovies(), favoritesRepository.get()) { movies, favorites ->
        movies.map {
            it.copy(isFavorite = favorites.contains(it.id))
        }
    }
    val detailsId: MutableStateFlow<Long> = MutableStateFlow(-1)

    suspend fun refresh() {
        movieRepository.refresh()
    }

    fun addToFavorite(id: Long) {
        favoritesRepository.add(id)
    }

    fun removeFromFavorite(id: Long) {
        favoritesRepository.remove(id)
    }
}
