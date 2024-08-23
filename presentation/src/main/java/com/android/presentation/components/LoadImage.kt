package com.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun LoadImage(
    url: String,
    modifier: Modifier,
) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
        contentDescription = "Image",
        contentScale = ContentScale.FillBounds,
        modifier = modifier,
    )
}
