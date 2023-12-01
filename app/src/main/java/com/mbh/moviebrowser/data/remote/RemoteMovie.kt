package com.mbh.moviebrowser.data.remote

import com.google.gson.annotations.SerializedName

class RemoteMovie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("genre_ids") val genres: Array<Long>,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val coverUrl: String,
    @SerializedName("vote_average") val rating: Float
)