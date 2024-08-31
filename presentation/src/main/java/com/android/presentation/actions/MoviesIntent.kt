package com.android.presentation.actions

import com.android.common.utils.Pagination
import com.android.domain.model.MovieItem

sealed class MoviesIntent {
    data class LoadMovies(
        val language: String,
        val page: Int = Pagination.INIT_PAGE.page,
    ) : MoviesIntent()

    data class NavigateToDetails(
        val responseMovieItem: MovieItem,
    ) : MoviesIntent()
}
