package com.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.common.utils.Constants
import com.android.domain.model.MovieItem
import com.android.presentation.MoviesViewModel
import com.android.presentation.R
import com.android.presentation.actions.MoviesIntent
import com.android.presentation.dimen.Dimensions

@Composable
fun MoviesList(
    innerPadding: PaddingValues,
    movies: List<MovieItem>?,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    val localDim = compositionLocalOf { Dimensions }
    val listState = rememberLazyListState()

    val isBottomReached by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index == movies?.lastIndex
        }
    }

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

            LazyColumn(
                state = listState,
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(localDim.current.marginMedium),
            ) {
                itemsIndexed(movies) { _, movie ->
                    MovieItemCard(
                        item = movie,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            LaunchedEffect(isBottomReached) {
                val isAtBottom =
                    listState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index == movies.lastIndex

                if (isAtBottom) {
                    viewModel.processIntent(
                        MoviesIntent
                            .LoadMovies(Constants.MOVIE_LANG.get),
                    )
                }
            }
        }
    }
}
