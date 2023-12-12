package com.mbh.moviebrowser.data

import com.mbh.moviebrowser.data.remote.Genre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor() {

    var genres = emptyList<Genre>()
        private set

    fun addAll(genres: List<Genre>) {
        this.genres.toMutableList().also {
            it.addAll(genres)
            this.genres = it
        }
    }

    fun clearAll() {
        genres = emptyList()
    }
}