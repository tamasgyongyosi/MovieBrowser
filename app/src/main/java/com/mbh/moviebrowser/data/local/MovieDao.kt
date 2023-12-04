package com.mbh.moviebrowser.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY position ASC")
    fun pagingSource(): PagingSource<Int, LocalMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<LocalMovie>)

    @Query("DELETE FROM movie")
    suspend fun clearAll()
}