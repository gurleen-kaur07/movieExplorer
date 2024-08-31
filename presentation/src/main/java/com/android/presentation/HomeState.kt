package com.android.presentation

import com.android.domain.model.MovieItem
import com.android.domain.util.Resource

sealed class HomeState {
    data class Loading(
        val isLoading: Boolean,
    ) : HomeState()

    data object NONE : HomeState()

    data class ResultAllMovies(
        val data: Resource<List<MovieItem>>,
    ) : HomeState()

    data class Exception(
        val callErrors: String,
    ) : HomeState()
}
