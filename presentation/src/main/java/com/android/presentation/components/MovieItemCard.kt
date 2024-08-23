package com.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.android.domain.model.MovieItem
import com.android.presentation.BuildConfig
import com.android.presentation.R
import com.android.presentation.Screen
import com.android.presentation.dimen.Dimensions

@Composable
fun MovieItemCard(
    index: Int,
    item: MovieItem?,
    modifier: Modifier,
    navController: NavController,
) {
    val LocalDim = compositionLocalOf { Dimensions() }

    Card(
        modifier =
            Modifier
                .padding(LocalDim.current.spaceSmall)
                .background(color = Color.White)
                .clickable {
                    navController.currentBackStackEntry?.savedStateHandle?.set("movieItem", item)
                    navController.navigate(Screen.MovieDetailsScreen.route)
                },
        shape = RoundedCornerShape(LocalDim.current.spaceSmall),
    ) {
        Column(
            modifier = modifier,
        ) {
            LoadImage(
                BuildConfig.PREFIX_IMAGE_URL + item?.backdropPath,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(LocalDim.current.spaceSmall))
            val lineHeight = MaterialTheme.typography.h6.fontSize * 4 / 3
            Text(
                text = stringResource(id = R.string.movie_name) + item?.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = LocalDim.current.spaceSmall),
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                lineHeight = lineHeight,
            )
            Text(
                text = LocalContext.current.getString(R.string.release_date, item?.releaseDate),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = LocalDim.current.spaceSmall),
            )
            StarRating(rating = (item?.voteAverage ?: 1.0) / 2)
            Spacer(modifier = Modifier.height(LocalDim.current.spaceSmall))
        }
    }
}
