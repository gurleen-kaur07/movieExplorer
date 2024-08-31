package com.android.domain.usecase

import com.android.domain.model.UpcomingMovies
import com.android.domain.repository.MoviesRepository
import com.android.domain.util.Resource
import javax.inject.Inject

class UpcomingMovieUseCase
@Inject
constructor(
    private val moviesRepository: MoviesRepository,
) {
    suspend operator fun invoke(
        lang: String,
        page: Int,
    ): Resource<UpcomingMovies> =
        moviesRepository.upcomingMovies(lang, page)
}
