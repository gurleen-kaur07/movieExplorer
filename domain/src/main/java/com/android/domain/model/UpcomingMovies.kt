package com.android.domain.model

data class UpcomingMovies(
    val dates: Dates,
    val page: Int,
    val totalPages: Int,
    val results: List<MovieItem>,
    val totalResults: Int,
)
