package com.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.domain.model.MovieItem
import com.android.presentation.Screen
import com.android.presentation.details.DetailScreen
import com.android.presentation.home.MoviesScreen
import com.android.presentation.theme.AssignmentApplicationTheme

@Composable
fun Navigation() {
    val navController = rememberNavController()
    AssignmentApplicationTheme {
        NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
            composable(Screen.Dashboard.route) {
                MoviesScreen(
                    navController = navController,
                )
            }

            composable(
                route = Screen.MovieDetailsScreen.route + "?movieId={movieId}&moviesTitle={moviesTitle}",
                arguments =
                listOf(
                    navArgument(name = "movieId") {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                    navArgument(name = "moviesTitle") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) {
                val item: MovieItem? =
                    navController.previousBackStackEntry?.savedStateHandle?.get("movieItem")
                DetailScreen(navController = navController, movie = item)
            }
        }
    }
}
