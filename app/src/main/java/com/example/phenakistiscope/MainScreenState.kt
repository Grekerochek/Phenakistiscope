package com.example.phenakistiscope

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

@Stable
internal data class MainScreenState(
    val currentColor: Color = Color.Black,
    val currentDrawStyle: DrawStyle = Stroke(1f),
    val currentInstrument: Instrument = Instrument.Pencil,
    val pathLists: List<List<PathData>> = emptyList(),
    val currentScreen: CurrentScreen = CurrentScreen.Edit,
    val currentIndex: Int = 0,
)

@Stable
internal data class DrawScreenState(
    val currentColor: Color = Color.Black,
    val currentDrawStyle: DrawStyle = Stroke(1f),
    val currentInstrument: Instrument = Instrument.Pencil,
)

@Stable
internal data class PathData(
    val path: Path = Path(),
    val color: Color = Color.Black,
    val drawStyle: DrawStyle = Stroke(1f),
)

internal enum class Instrument {
    Pen, Pencil, Erase, Circle, Triangle, Square
}

internal enum class CurrentScreen {
    Edit, Play
}
