package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.BuildConfig
import com.mbh.moviebrowser.data.remote.GenresResponse
import com.mbh.moviebrowser.data.remote.RemoteMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(@Query("page") page: Int = 1, @Query("api_key") apiKey: String = BuildConfig.API_KEY): RemoteMoviesResponse

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String = BuildConfig.API_KEY): GenresResponse
}