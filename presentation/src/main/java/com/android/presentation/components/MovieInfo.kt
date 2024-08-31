package com.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.android.domain.model.MovieItem
import com.android.presentation.R
import com.android.presentation.dimen.Dimensions
import java.util.Locale

@Composable
fun MovieInfo(movie: MovieItem) {
    val localDim = compositionLocalOf { Dimensions }

    Column(
        modifier = Modifier.padding(localDim.current.spaceMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(localDim.current.spaceLarge))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.h5,
            overflow = TextOverflow.Ellipsis,
            modifier =
            Modifier
                .padding(horizontal = localDim.current.spaceLarge)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = localDim.current.spaceMedium),
            horizontalArrangement = Arrangement.Center,
        ) {
            val date =
                LocalContext.current.getString(
                    R.string.release_date,
                    movie.releaseDate + " (" + movie.originalLanguage.uppercase(Locale.ROOT) + ")",
                )
            Text(
                text = date,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                modifier = Modifier.padding(end = localDim.current.spaceSmall),
            )
        }

        StarRating(rating = (movie.voteAverage) / 2)
        Spacer(modifier = Modifier.height(localDim.current.spaceMedium))
        Text(
            text = stringResource(id = R.string.overview),
            style = MaterialTheme.typography.h5,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(localDim.current.spaceSmall))
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.body1,
        )
    }
}
