package com.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.domain.model.MovieItem
import com.android.presentation.BuildConfig
import com.android.presentation.MoviesViewModel
import com.android.presentation.R
import com.android.presentation.actions.MoviesIntent
import com.android.presentation.dimen.Dimensions

@Composable
fun MovieItemCard(
    item: MovieItem,
    modifier: Modifier,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    val localDim = compositionLocalOf { Dimensions }

    Card(
        modifier =
            Modifier
                .padding(localDim.current.spaceSmall)
                .background(color = Color.White)
                .clickable {
                    viewModel.processIntent(MoviesIntent.NavigateToDetails(item))
                },
        shape = RoundedCornerShape(localDim.current.spaceSmall),
    ) {
        Column(
            modifier = modifier,
        ) {
            LoadImage(
                BuildConfig.PREFIX_IMAGE_URL + item.backdropPath.orEmpty(),
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(localDim.current.spaceSmall))
            val lineHeight = MaterialTheme.typography.h6.fontSize * 4 / 3
            Text(
                text = stringResource(id = R.string.movie_name) + item.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = localDim.current.spaceSmall),
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                lineHeight = lineHeight,
            )
            Text(
                text = LocalContext.current.getString(R.string.release_date, item.releaseDate),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = localDim.current.spaceSmall),
            )
            StarRating(rating = (item.voteAverage) / 2)
            Spacer(modifier = Modifier.height(localDim.current.spaceSmall))
        }
    }
}
