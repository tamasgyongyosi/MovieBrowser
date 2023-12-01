package com.mbh.moviebrowser.data.remote

import com.google.gson.annotations.SerializedName

class GenresResponse (
    @SerializedName("genres")
    val results: List<Genre>
)