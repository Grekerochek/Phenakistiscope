package com.example.phenakistiscope

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

@Stable
internal data class MainScreenState(
    val currentColor: Color = Color.Black,
    val previousColor: Color = Color.Black,
    val currentDrawStyle: DrawStyle = Stroke(10f),
    val currentInstrument: Instrument = Instrument.Pencil,
    val currentScreen: CurrentScreen = CurrentScreen.Edit,
    val frameWidth: Int = 500,
    val frameHeight: Int = 500,
    val currentIndex: Long = 0,
    val currentBitmap: ImageBitmap? = null,
    val previousBitmap: ImageBitmap? = null,
    val bitmapForCopy: ImageBitmap? = null,
    val currentPlayingBitmap: ImageBitmap? = null,
    val speed: Int = 1,
    val isGenerating: Boolean = false,
    val isGeneratePanelOpened: Boolean = false,
) {

    val blendMode: BlendMode get() = if (currentInstrument == Instrument.Eraser) {
        BlendMode.Clear
    } else {
        BlendMode.SrcOver
    }

    val isBinEnabled: Boolean get() = currentScreen == CurrentScreen.Edit && currentIndex > 0

    val isPauseEnabled: Boolean get() = currentScreen == CurrentScreen.Play

    val playState: InstrumentState
        get() = if (currentScreen == CurrentScreen.Play) {
            InstrumentState.SELECTED
        } else if (currentIndex < 2) {
            InstrumentState.DISABLED
        } else {
            InstrumentState.ENABLED
        }

    val pencilState: InstrumentState
        get() = if (currentScreen == CurrentScreen.Play) {
            InstrumentState.DISABLED
        } else if (currentInstrument == Instrument.Pencil) {
            InstrumentState.SELECTED
        } else {
            InstrumentState.ENABLED
        }

    val eraserState: InstrumentState
        get() = if (currentScreen == CurrentScreen.Play) {
            InstrumentState.DISABLED
        } else if (currentInstrument == Instrument.Eraser) {
            InstrumentState.SELECTED
        } else {
            InstrumentState.ENABLED
        }

    val isAddFrameEnabled: Boolean
        get() = currentScreen == CurrentScreen.Edit && currentIndex <= Int.MAX_VALUE

    val isCopyFrameEnabled: Boolean
        get() = isAddFrameEnabled && currentIndex > 0
}

enum class InstrumentState {
    SELECTED, ENABLED, DISABLED,
}

@Stable
internal class Frame(
    val bitmap: ImageBitmap? = null,
    val pathList: List<PathData> = emptyList(),
)

@Stable
internal data class PathData(
    val path: Path = Path(),
    val color: Color = Color.Black,
    val drawStyle: DrawStyle = Stroke(16f),
    val blendMode: BlendMode = BlendMode.SrcOver,
)

internal enum class Instrument {
    Pencil, Eraser
}

internal enum class CurrentScreen {
    Edit, Play
}
