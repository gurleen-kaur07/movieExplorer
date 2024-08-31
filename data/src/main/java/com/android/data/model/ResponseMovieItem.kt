package com.android.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseMovieItem(
    @field:SerializedName("overview")
    val overview: String,
    @field:SerializedName("id")
    val movieId: String,
    @field:SerializedName("original_language")
    val originalLanguage: String,
    @field:SerializedName("original_title")
    val originalTitle: String,
    @field:SerializedName("video")
    val video: Boolean,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("poster_path")
    val posterPath: String,
    @field:SerializedName("backdrop_path")
    val backdropPath: String,
    @field:SerializedName("release_date")
    val releaseDate: String,
    @field:SerializedName("popularity")
    val popularity: Double,
    @field:SerializedName("vote_average")
    val voteAverage: Double,
    @field:SerializedName("adult")
    val adult: Boolean,
    @field:SerializedName("vote_count")
    val voteCount: Int,
) : Parcelable
