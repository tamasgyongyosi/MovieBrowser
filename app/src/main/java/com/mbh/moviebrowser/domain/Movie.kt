package com.mbh.moviebrowser.domain

data class Movie(
    val id: Long,
    val title: String,
    val genres: String,
    val overview: String?,
    val coverUrl: String?,
    val rating: Float,
    val isFavorite: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (genres != other.genres) return false
        if (overview != other.overview) return false
        if (coverUrl != other.coverUrl) return false
        if (rating != other.rating) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + (coverUrl?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}
