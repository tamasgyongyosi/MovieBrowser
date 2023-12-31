package com.mbh.moviebrowser.data

import com.mbh.moviebrowser.data.local.LocalMovie
import com.mbh.moviebrowser.data.remote.Genre
import com.mbh.moviebrowser.data.remote.RemoteMovie
import com.mbh.moviebrowser.domain.Movie

fun RemoteMovie.toLocal(genres: List<Genre>) = LocalMovie(
    id = id,
    title = title,
    genres = this.genres.joinToString { genreId ->
        val genre = genres.firstOrNull { genre -> genre.id == genreId }
        genre?.name ?: "Unknown"
    },
    overview = overview,
    coverUrl = coverUrl,
    rating = rating
)

@JvmName("remoteToLocal")
fun List<RemoteMovie>.toLocal(genres: List<Genre>) = map { it.toLocal(genres) }

fun LocalMovie.toExternal() = Movie(
    id = id,
    title = title,
    genres = genres,
    overview = overview,
    coverUrl = coverUrl,
    rating = rating
)

@JvmName("localToExternal")
fun List<LocalMovie>.toExternal() = map(LocalMovie::toExternal)