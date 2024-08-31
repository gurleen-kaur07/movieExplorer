package com.android.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.presentation.dimen.Dimensions

@Composable
fun StarRating(
    rating: Double,
    totalStars: Int = 5,
) {
    val localDim = compositionLocalOf { Dimensions }

    Row(
        modifier = Modifier.padding(horizontal = localDim.current.spaceSmall),
    ) {
        for (i in 1..totalStars) {
            val starIcon = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = starIcon,
                contentDescription = null,
                modifier =
                Modifier
                    .size(localDim.current.marginLarge),
                tint = if (i <= rating) MaterialTheme.colorScheme.primary else Color.Gray,
            )
        }
    }
}
