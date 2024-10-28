package com.example.phenakistiscope

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

data class DrawScreenState(
    val lines: List<Line> = emptyList(),
    val currentColor: Color = Color.Black,
    val currentDrawStyle: DrawStyle = Stroke(1f),
    val currentInstrument: Instrument = Instrument.Pencil,
)

data class PathData(
    val path: Path = Path(),
    val color: Color = Color.Black,
    val drawStyle: DrawStyle = Stroke(1f),
)

enum class Instrument {
    Pen, Pencil, Erase, Circle, Triangle, Square
}
