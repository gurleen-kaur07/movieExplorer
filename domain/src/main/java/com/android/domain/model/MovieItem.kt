package com.android.domain.model

import java.io.Serializable

data class MovieItem(
    val overview: String,
    val movieId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val video: Boolean,
    val title: String,
    val posterPath: String,
    val backdropPath: String?,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val adult: Boolean,
    val voteCount: Int,
) : Serializable
