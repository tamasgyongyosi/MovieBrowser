package com.mbh.moviebrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemoteKey")
data class RemoteKey(
    @PrimaryKey val movieId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)