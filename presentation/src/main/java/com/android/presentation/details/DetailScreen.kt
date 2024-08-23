package com.android.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import com.android.domain.model.MovieItem
import com.android.presentation.BuildConfig
import com.android.presentation.components.LoadImage
import com.android.presentation.components.MovieInfo
import com.android.presentation.components.TopBar
import com.android.presentation.dimen.Dimensions

@Composable
fun DetailScreen(
    movie: MovieItem?,
    navController: NavHostController,
) {
    val LocalDim = compositionLocalOf { Dimensions() }

    Scaffold(
        topBar = {
            TopBar(title = movie?.title ?: "") {
                navController.popBackStack()
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding),
            ) {
                LazyColumn(content = {
                    item {
                        LoadImage(
                            url = (
                                BuildConfig.PREFIX_IMAGE_URL +
                                    movie?.posterPath
                            ),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(LocalDim.current.spaceSmall)
                                    .clip(
                                        shape =
                                            androidx.compose.foundation.shape
                                                .RoundedCornerShape(LocalDim.current.spaceMedium),
                                    ),
                        )
                    }
                    item {
                        if (movie != null) {
                            MovieInfo(movie = movie)
                        }
                    }
                })
            }
        }
    }
}
