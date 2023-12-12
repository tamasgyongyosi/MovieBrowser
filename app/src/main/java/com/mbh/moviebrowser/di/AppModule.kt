package com.mbh.moviebrowser.di

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.mbh.moviebrowser.BuildConfig
import com.mbh.moviebrowser.api.MovieApi
import com.mbh.moviebrowser.config.Config
import com.mbh.moviebrowser.config.Config.NETWORK_TIME_OUT
import com.mbh.moviebrowser.config.Config.PAGE_SIZE
import com.mbh.moviebrowser.data.MovieRemoteMediator
import com.mbh.moviebrowser.data.local.LocalMovie
import com.mbh.moviebrowser.data.local.MovieDao
import com.mbh.moviebrowser.data.local.MovieDatabase
import com.mbh.moviebrowser.data.local.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            if (BuildConfig.DEBUG) {
                Log.i("OKHTTP", message)
            }
        }
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Config.MOVIES_BASE_URL)
            .build()
    }

    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "Movies.db"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    fun provideRemoteKeyDao(database: MovieDatabase): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    @Singleton
    fun provideMoviePager(
        movieDao: MovieDao,
        movieRemoteMediator: MovieRemoteMediator
    ): Pager<Int, LocalMovie> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 2 * PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = movieRemoteMediator,
            pagingSourceFactory = {
                movieDao.pagingSource()
            }
        )
    }
}