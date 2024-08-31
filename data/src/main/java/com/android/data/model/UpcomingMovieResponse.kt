package com.android.data.model

import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    @field:SerializedName("dates")
    val responseDates: ResponseDates,
    @field:SerializedName("page")
    val page: Int,
    @field:SerializedName("total_pages")
    val totalPages: Int,
    @field:SerializedName("results")
    val results: List<ResponseMovieItem>,
    @field:SerializedName("total_results")
    val totalResults: Int,
)
