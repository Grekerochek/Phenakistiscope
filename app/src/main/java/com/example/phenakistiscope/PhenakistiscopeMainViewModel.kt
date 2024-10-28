package com.example.phenakistiscope

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PhenakistiscopeMainViewModel : ViewModel() {

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
}
