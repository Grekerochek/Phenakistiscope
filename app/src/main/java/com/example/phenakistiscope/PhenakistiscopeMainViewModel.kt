package com.example.phenakistiscope

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class PhenakistiscopeMainViewModel : ViewModel() {

    private val _currentStateScreen: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState())

    val currentScreenState: StateFlow<MainScreenState> = _currentStateScreen

    fun onAddFileClicked(pathList: List<PathData>) {
        _currentStateScreen.update {
            val newPathList = it.pathLists.toMutableList().apply { add(pathList) }

            it.copy(
                pathLists = newPathList,
            )
        }
    }

    fun onPlayClicked() {
        _currentStateScreen.update { it.copy(currentScreen = CurrentScreen.Play) }
    }

    fun onPauseClicked() {
        _currentStateScreen.update { it.copy(currentScreen = CurrentScreen.Edit) }
    }

    fun upCurrentIndex() {
        _currentStateScreen.update {
            it.copy(
                currentIndex = if (it.currentIndex + 1 == it.pathLists.size) {
                    0
                } else {
                    it.currentIndex + 1
                }
            )
        }
    }
}
