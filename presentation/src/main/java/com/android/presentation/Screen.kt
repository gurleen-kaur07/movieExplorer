package com.android.presentation

sealed class Screen(
    val route: String,
) {
    data object Dashboard : Screen("dashboard_screen")

    data object MovieDetailsScreen : Screen("movie_details_screen")
}
