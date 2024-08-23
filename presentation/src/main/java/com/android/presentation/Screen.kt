package com.android.presentation

sealed class Screen(
    val route: String,
) {
    object Dashboard : Screen("dashboard_screen")

    object MovieDetailsScreen : Screen("movie_details_screen")
}
