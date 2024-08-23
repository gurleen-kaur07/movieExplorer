package com.android.domain.model

import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    @field:SerializedName("dates")
    val dates: Dates,
    @field:SerializedName("page")
    val page: Int,
    @field:SerializedName("total_pages")
    val totalPages: Int,
    @field:SerializedName("results")
    val results: List<MovieItem>,
    @field:SerializedName("total_results")
    val totalResults: Int,
)
