package com.mbh.moviebrowser.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun pagingSource(): PagingSource<Int, LocalMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<LocalMovie>)

    @Query("DELETE FROM movie")
    suspend fun clearAll()

    @Query("SELECT * FROM movie WHERE id=:id")
    fun getById(id: Long): Flow<LocalMovie>
}