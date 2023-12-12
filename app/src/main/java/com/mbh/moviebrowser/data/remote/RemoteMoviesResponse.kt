package com.mbh.moviebrowser.data.remote

import com.google.gson.annotations.SerializedName

class RemoteMoviesResponse(
    @SerializedName("results") val results: List<RemoteMovie> = emptyList()
)