package com.android.presentation

import com.android.domain.model.MovieItem
import com.android.domain.util.Resource

sealed class HomeState {
    data class Loading(
        val isLoading: Boolean,
    ) : HomeState()

    object NONE : HomeState()

    data class ResultAllMovies(
        val data: Resource<List<MovieItem>>,
    ) : HomeState()

    data class SideEffect(
        val effects: AppEffect,
    ) : HomeState()

    data class Exception(
        val callErrors: String,
    ) : HomeState()
}
