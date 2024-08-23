package com.android.domain.repository

import com.android.domain.model.UpcomingMovieResponse
import com.android.domain.util.Resource

interface MoviesRepository {
    suspend fun upcomingMovies(
        lang: String,
        page: Int,
    ): Resource<UpcomingMovieResponse>
}
