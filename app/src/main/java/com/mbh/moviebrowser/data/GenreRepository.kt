package com.mbh.moviebrowser.data

import com.mbh.moviebrowser.api.MovieApi
import com.mbh.moviebrowser.data.remote.Genre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    private val genres = ArrayList<Genre>()

    suspend fun get(): List<Genre> {
        return if (genres.isEmpty()) {
            movieApi.getGenres().results.also {
                genres.addAll(it)
            }
        } else {
            genres
        }
    }
}