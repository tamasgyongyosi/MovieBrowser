package com.mbh.moviebrowser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM RemoteKey WHERE movieId = :movieId")
    fun remoteKeyByMovieId(movieId: Long): RemoteKey?

    @Query("DELETE FROM RemoteKey")
    fun clearAll()
}