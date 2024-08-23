package com.android.data.repository

import com.android.data.source.remote.api.ApiService
import com.android.domain.model.UpcomingMovieResponse
import com.android.domain.repository.MoviesRepository
import com.android.domain.util.Resource
import com.android.domain.util.SafeCall.safeCall
import javax.inject.Inject

class MoviesRepositoryImpl
    @Inject
    constructor(
        private val apiService: ApiService,
    ) : MoviesRepository {
        override suspend fun upcomingMovies(
            lang: String,
            page: Int,
        ): Resource<UpcomingMovieResponse> =
            safeCall {
                apiService.getUpcoming(
                    page = page,
                    language = lang,
                )
            }
    }
