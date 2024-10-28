package com.example.phenakistiscope.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
internal fun PreviousFrame(imageBitmap: ImageBitmap) {
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer(alpha = 0.99F)
        ) {
            drawImage(imageBitmap)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = 20.dp))
                .background(
                    color = Color.White.copy(alpha = 0.4f) // used for better design
                )
        )
    }
}
