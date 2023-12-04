package com.mbh.moviebrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalMovie::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun remoteKeyDao(): RemoteKeyDao
}