package com.android.data.source.remote.api

import com.android.domain.model.UpcomingMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("page") page: Int?,
        @Query("language") language: String?,
    ): UpcomingMovieResponse
}
