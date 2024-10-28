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
    val frames: List<Frame> = emptyList(),
    val currentScreen: CurrentScreen = CurrentScreen.Edit,
    val currentEditFrame: Frame? = null,
) {
    fun getPreviousPathList(): List<PathData>? {
        return if (frames.isEmpty()) {
            null
        } else {
            frames.last().pathList
        }
    }

    val isBinEnabled get() = currentScreen == CurrentScreen.Edit && frames.isNotEmpty()

    val isAddFrameEnabled get() = currentScreen == CurrentScreen.Edit

    val isPauseEnabled get() = currentScreen == CurrentScreen.Play

    val playState get() = if (currentScreen == CurrentScreen.Play) {
        InstrumentState.SELECTED
    } else if (frames.size < 2) {
        InstrumentState.DISABLED
    } else {
        InstrumentState.ENABLED
    }

    val pencilState get() = if (currentScreen == CurrentScreen.Play) {
        InstrumentState.DISABLED
    } else if (currentInstrument == Instrument.Pencil) {
        InstrumentState.SELECTED
    } else {
        InstrumentState.ENABLED
    }

    val eraseState get() = if (currentScreen == CurrentScreen.Play) {
        InstrumentState.DISABLED
    } else if (currentInstrument == Instrument.Erase) {
        InstrumentState.SELECTED
    } else {
        InstrumentState.ENABLED
    }
}

enum class InstrumentState {
    SELECTED, ENABLED, DISABLED,
}

@Stable
internal data class Frame(
    val pathList: List<PathData> = emptyList(),
    val removedPathList: List<PathData> = emptyList(),
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
    Pencil, Erase
}

internal enum class CurrentScreen {
    Edit, Play
}
