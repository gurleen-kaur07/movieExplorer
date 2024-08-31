package com.android.data.repository

import com.android.data.mapper.ResponseMapper
import com.android.data.source.remote.api.ApiService
import com.android.domain.model.UpcomingMovies
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
    ): Resource<UpcomingMovies> =
        safeCall {
            ResponseMapper.mapToDomainModel(
                apiService.getUpcoming(
                    page = page,
                    language = lang,
                ),
            )
        }
}
