package com.example.phenakistiscope.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer


@Composable
internal fun PlayScreenContent(
    imageBitmap: ImageBitmap?,
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .graphicsLayer(alpha = 0.99F)
    ) {
        if (imageBitmap != null && !imageBitmap.asAndroidBitmap().isRecycled) {
            drawImage(imageBitmap)
        }
    }
}
