package com.android.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.android.presentation.dimen.Dimensions

@Composable
fun TopBar(
    isBackPressRequired: Boolean = true,
    title: String,
    onBack: () -> Unit,
) {
    val LocalDim = compositionLocalOf { Dimensions() }

    TopAppBar(
        backgroundColor = Color.White,
        elevation = LocalDim.current.default,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iconSize = LocalDim.current.spaceExtraLarge

            val icon = if (isBackPressRequired) Icons.Filled.Close else Icons.Default.Home
            Icon(
                imageVector = icon,
                contentDescription = "ArrowBack",
                modifier =
                    Modifier
                        .padding(start = LocalDim.current.spaceMedium)
                        .clickable {
                            onBack()
                        }.size(iconSize),
            )

            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontSize = 20.sp,
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            Box(
                modifier =
                    Modifier
                        .padding(end = LocalDim.current.spaceMedium)
                        .size(iconSize),
            ) { }
        }
    }
}
