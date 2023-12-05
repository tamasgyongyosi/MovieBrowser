package com.mbh.moviebrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class LocalMovie(
    @PrimaryKey(autoGenerate = true) val position: Long = 0,
    val id: Long,
    val title: String,
    val genres: String,
    val overview: String,
    val coverUrl: String,
    val rating: Float
)