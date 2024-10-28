package com.example.phenakistiscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import kotlinx.coroutines.delay

@Composable
internal fun PlayScreenContent(
    mainScreenState: MainScreenState,
    upCurrentIndex: () -> Unit
) {
    LaunchedEffect(Unit) {
        while (mainScreenState.currentScreen == CurrentScreen.Play) {
            delay(1000)
            upCurrentIndex()
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        mainScreenState.pathLists[mainScreenState.currentIndex].forEach { path ->
            drawPath(
                path = path.path,
                color = path.color,
                style = path.drawStyle,
            )
        }
    }
}
