package com.mbh.moviebrowser.data

import com.mbh.moviebrowser.api.MovieApi
import com.mbh.moviebrowser.data.remote.Genre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    private var genres = emptyList<Genre>()

    suspend fun get(): List<Genre> {
        return genres.ifEmpty {
            movieApi.getGenres().results.also { results ->
                genres.toMutableList().also {
                    it.addAll(results)
                    genres = it
                }
            }
        }
    }
}