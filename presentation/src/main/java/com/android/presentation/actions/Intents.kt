package com.android.presentation.actions

import com.android.common.utils.Pagination

sealed class MoviesIntent {
    data class LoadMovies(
        val language: String,
        val page: Int = Pagination.INIT_PAGE.page,
    ) : MoviesIntent()
}
