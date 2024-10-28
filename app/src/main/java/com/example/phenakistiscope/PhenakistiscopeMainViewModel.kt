package com.example.phenakistiscope

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class PhenakistiscopeMainViewModel : ViewModel() {

    private val _currentScreenState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState())

    val currentScreenState: StateFlow<MainScreenState> = _currentScreenState

    fun onAddFrameClicked(frame: Frame) {
        if (!currentScreenState.value.isAddFrameEnabled) {
            return
        }
        _currentScreenState.update {
            val newPathList = it.frames.toMutableList().apply { add(frame) }

            it.copy(
                frames = newPathList,
            )
        }
    }

    fun onRemoveFrameClicked() {
        if (!currentScreenState.value.isBinEnabled) {
            return
        }
        _currentScreenState.update {
            val currentEditFrame = it.frames.last()
            val newFrames = it.frames.dropLast(1)

            it.copy(
                currentEditFrame = currentEditFrame,
                frames = newFrames
            )
        }
    }

    fun frameEdited() {
        _currentScreenState.update { it.copy(currentEditFrame = null) }
    }

    fun onPlayClicked() {
        if (currentScreenState.value.playState == InstrumentState.ENABLED) {
            _currentScreenState.update { it.copy(currentScreen = CurrentScreen.Play) }
        }
    }

    fun onPauseClicked() {
        if (!currentScreenState.value.isPauseEnabled) {
            return
        }
        _currentScreenState.update {
            it.copy(currentScreen = CurrentScreen.Edit)
        }
    }

    fun onInstrumentClicked(instrument: Instrument) {
        if (instrument == currentScreenState.value.currentInstrument) {
            return
        }
        if (currentScreenState.value.currentScreen == CurrentScreen.Edit) {
            _currentScreenState.update {
                val color = if (instrument == Instrument.Eraser) Color.White else it.previousColor
                val previousColor = it.currentColor
                it.copy(currentInstrument = instrument, currentColor = color, previousColor = previousColor)
            }
        }
    }

    fun changeColor(color: Color) {
        _currentScreenState.update { it.copy(currentColor = color) }
    }
}
