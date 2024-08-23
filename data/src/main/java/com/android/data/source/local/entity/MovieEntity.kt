package com.android.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val overview: String,
    val movieId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val isVideo: Boolean,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val isAdult: Boolean,
    val voteCount: Int,
)
