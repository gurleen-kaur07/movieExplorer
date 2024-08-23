package com.android.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.common.utils.Constants
import com.android.presentation.AppEffect
import com.android.presentation.HomeState
import com.android.presentation.MoviesViewModel
import com.android.presentation.R
import com.android.presentation.actions.MoviesIntent
import com.android.presentation.components.GenericMessage
import com.android.presentation.components.Loader
import com.android.presentation.components.MovieItemCard
import com.android.presentation.components.TopBar
import com.android.presentation.dimen.Dimensions

@Composable
fun HomeScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    navController: NavHostController,
) {
    val viewState by viewModel.uiStates.collectAsState(initial = HomeState.NONE)
    val context = LocalContext.current
    val LocalDim = compositionLocalOf { Dimensions() }

    LaunchedEffect(Unit) {
        viewModel.processIntent(
            MoviesIntent
                .LoadMovies(Constants.MOVIE_LANG.get),
        )
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = R.string.upcoming_movies), isBackPressRequired = false) {
                navController.popBackStack()
            }
        },
    ) { innerPadding ->
        when (viewState) {
            is HomeState.Exception -> {
                GenericMessage((viewState as HomeState.Exception).callErrors)
            }
            is HomeState.Loading -> {
                if ((viewState as HomeState.Loading).isLoading) {
                    Loader()
                }
            }
            HomeState.NONE -> {}
            is HomeState.SideEffect -> {
                when (val effects = (viewState as HomeState.SideEffect).effects) {
                    AppEffect.NONE -> {}
                    is AppEffect.NetworkError -> {
                        Toast.makeText(context, effects.message, Toast.LENGTH_SHORT).show()
                    }
                    is AppEffect.ShowToast -> {
                        Toast.makeText(context, effects.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is HomeState.ResultAllMovies -> {
                val movies = ((viewState as HomeState.ResultAllMovies).data).data
                if (movies != null) {
                    if (movies.isEmpty()) {
                        GenericMessage(message = stringResource(id = R.string.no_data))
                    } else {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.onPrimary),
                        )
                        val listState = rememberLazyListState()
                        val isLoading by viewModel.isPaginating.collectAsState()
                        LazyColumn(
                            state = listState,
                            contentPadding = innerPadding,
                            verticalArrangement = Arrangement.spacedBy(LocalDim.current.marginMedium),
                        ) {
                            itemsIndexed(movies) { index, movie ->
                                MovieItemCard(
                                    index = index,
                                    item = movie,
                                    modifier = Modifier.fillMaxWidth(),
                                    navController = navController,
                                )
                            }
                            if (isLoading) {
                                item {
                                    Loader()
                                }
                            }
                        }
                        LaunchedEffect(listState.firstVisibleItemIndex) {
                            val isAtBottom =
                                listState.layoutInfo.visibleItemsInfo
                                    .lastOrNull()
                                    ?.index == movies.lastIndex
                            if (isAtBottom && !isLoading) {
                                viewModel.loadMoreItems(Constants.MOVIE_LANG.get)
                            }
                        }
                    }
                }
            }
        }
    }
}
