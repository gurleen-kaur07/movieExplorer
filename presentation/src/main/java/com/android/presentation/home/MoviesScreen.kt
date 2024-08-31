package com.android.presentation.home

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.common.utils.Constants
import com.android.presentation.AppEffect
import com.android.presentation.HomeState
import com.android.presentation.MoviesViewModel
import com.android.presentation.R
import com.android.presentation.Screen
import com.android.presentation.actions.MoviesIntent
import com.android.presentation.components.GenericMessage
import com.android.presentation.components.Loader
import com.android.presentation.components.MoviesList
import com.android.presentation.components.TopBar


@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val viewState by viewModel.uiStates.collectAsState(initial = HomeState.NONE)
    val uiEffect by viewModel.effect.collectAsState(initial = AppEffect.NONE)
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.processIntent(
            MoviesIntent
                .LoadMovies(Constants.MOVIE_LANG.get),
        )
    }

    LaunchedEffect(key1 = viewModel.navigator.value) {
        viewModel.navigator.value?.let {
            navController.currentBackStackEntry?.savedStateHandle?.set("movieItem", it)
            navController.navigate(Screen.MovieDetailsScreen.route)
            viewModel.navigator.value = null
        }
    }

    LaunchedEffect(key1 = uiEffect) {
        when (uiEffect) {
            is AppEffect.NONE -> {}

            is AppEffect.ShowToast -> {
                Toast.makeText(
                    context,
                    (uiEffect as AppEffect.ShowToast).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.upcoming_movies),
                isBackPressRequired = false
            ) {
                navController.popBackStack()
            }
        },
    ) { innerPadding ->
        when (viewState) {
            is HomeState.Exception -> {
                GenericMessage(
                    message = (viewState as HomeState.Exception).callErrors,
                    displayRetryButton = true
                ){
                    viewModel.processIntent(
                        MoviesIntent
                            .LoadMovies(Constants.MOVIE_LANG.get),
                    )
                }
            }

            is HomeState.Loading -> {
                if ((viewState as HomeState.Loading).isLoading) {
                    Loader()
                }
            }

            is HomeState.ResultAllMovies -> {
                val movies = ((viewState as HomeState.ResultAllMovies).data).data
                movies?.let {
                    MoviesList(innerPadding = innerPadding, movies = movies)
                } ?: GenericMessage(message = stringResource(id = R.string.no_data))
            }

            HomeState.NONE -> {}
        }
    }
}
