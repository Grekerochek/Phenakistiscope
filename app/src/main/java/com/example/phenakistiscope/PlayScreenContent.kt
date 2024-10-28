package com.example.phenakistiscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import kotlinx.coroutines.delay

@Composable
internal fun PlayScreenContent(mainScreenState: MainScreenState) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (mainScreenState.currentScreen == CurrentScreen.Play) {
            delay(1000)
            if (currentIndex + 1 == mainScreenState.pathLists.size) {
                currentIndex = 0
            } else {
                currentIndex += 1
            }
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        mainScreenState.pathLists[currentIndex].forEach { path ->
            drawPath(
                path = path.path,
                color = path.color,
                style = path.drawStyle,
            )
        }
    }
}
