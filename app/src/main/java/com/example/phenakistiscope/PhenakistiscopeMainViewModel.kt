package com.example.phenakistiscope

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


internal class PhenakistiscopeMainViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentScreenState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState())

    val currentScreenState: StateFlow<MainScreenState> = _currentScreenState

    fun onAddFrameClicked(frame: Frame) {
        if (!currentScreenState.value.isAddFrameEnabled) {
            return
        }
        viewModelScope.launch {
            addFrame(frame)
            if (currentScreenState.value.currentIndex - 1 >= 0) {
                loadBitmap(imageName = currentScreenState.value.currentIndex - 1, isForPrevious = true)
            }
        }
    }

    private fun addFrame(frame: Frame) {
        if (!currentScreenState.value.isAddFrameEnabled) {
            return
        }
        saveBitmap(
            name = currentScreenState.value.currentIndex,
            previousBitmap = frame.bitmap,
            pathList = frame.pathList,
        )
        _currentScreenState.update {
            it.copy(
                currentIndex = it.currentIndex + 1,
                currentBitmap = null,
            )
        }
    }

    fun updateWidthHeightOfTheFrame(width: Int, height: Int) {
        _currentScreenState.update {
            it.copy(
                frameWidth = width,
                frameHeight = height
            )
        }
    }

    fun onRemoveFrameClicked() {
        if (!currentScreenState.value.isBinEnabled) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            deleteBitmap(currentScreenState.value.currentIndex - 1)
            val newIndex = _currentScreenState.value.currentIndex - 1

            _currentScreenState.update {
                val currentEditBitmap = it.previousBitmap

                it.copy(
                    currentBitmap = currentEditBitmap,
                    currentIndex = newIndex,
                )
            }

            if (newIndex - 1 >= 0) {
                loadBitmap(imageName = newIndex - 1, true)
            } else {
                _currentScreenState.update {
                    it.copy(
                        previousBitmap = null
                    )
                }
            }
        }
    }

    private var playingJob: Job? = null

    fun onPlayClicked() {
        if (currentScreenState.value.playState == InstrumentState.ENABLED) {
            _currentScreenState.update { it.copy(currentScreen = CurrentScreen.Play) }
            playingJob = viewModelScope.launch(Dispatchers.IO) {
                while (currentScreenState.value.currentScreen == CurrentScreen.Play) {
                    for (i in 0..<currentScreenState.value.currentIndex) {
                        delay(
                            when (currentScreenState.value.speed) {
                                1 -> 200
                                2 -> 100
                                else -> 50
                            }
                        )
                        loadBitmap(imageName = i, null)
                    }
                }
            }
        }
    }

    fun onPauseClicked() {
        if (!currentScreenState.value.isPauseEnabled) {
            return
        }
        playingJob?.cancel()
        _currentScreenState.update {
            it.copy(currentScreen = CurrentScreen.Edit, currentPlayingBitmap = null)
        }
    }

    fun onInstrumentClicked(instrument: Instrument) {
        if (instrument == currentScreenState.value.currentInstrument) {
            return
        }
        if (currentScreenState.value.currentScreen == CurrentScreen.Edit) {
            _currentScreenState.update {
                val color =
                    if (instrument == Instrument.Eraser) Color.Transparent else it.previousColor
                val previousColor = it.currentColor
                it.copy(
                    currentInstrument = instrument,
                    currentColor = color,
                    previousColor = previousColor
                )
            }
        }
    }

    fun changeColor(color: Color) {
        _currentScreenState.update { it.copy(currentColor = color) }
    }

    fun changeSpeed(newSpeed: Int) {
        _currentScreenState.update {
            it.copy(
                speed = newSpeed
            )
        }
    }

    fun changeStyle(newStroke: Float) {
        _currentScreenState.update {
            it.copy(
                currentDrawStyle = Stroke(newStroke)
            )
        }
    }

    fun generateFrames(numberOfFrames: Int) {
        _currentScreenState.update { it.copy(isGenerating = true) }
        viewModelScope.launch(Dispatchers.Default) {
            var next = true
            var j = 110f

            val pathData = PathData(
                path = Path().apply {
                    addOval(
                        Rect(
                            Offset(
                                (currentScreenState.value.frameWidth / 2).toFloat(),
                                j
                            ), 100f
                        )
                    )
                },
            )

            for (i in 0 until numberOfFrames) {
                val newPathData = PathData(
                    path = Path().apply {
                        addPath(pathData.path)
                        translate(Offset(x = 0f, y = j))
                    },
                    color = pathData.color,
                    drawStyle = pathData.drawStyle,
                    blendMode = pathData.blendMode
                )
                if (j + 10 > currentScreenState.value.frameHeight) {
                    next = false
                } else if (i - 10 < 0) {
                    next = true
                }

                if (next) {
                    j += 10
                } else {
                    j -= 10
                }
                addFrame(frame = Frame(pathList = listOf(newPathData)))
            }

            loadBitmap(currentScreenState.value.currentIndex-1, true)
            _currentScreenState.update { it.copy(isGenerating = false, isGeneratePanelOpened = false) }
        }
    }

    private fun bitmapLoaded(bitmap: Bitmap?, isForPrevious: Boolean?) {
        if (bitmap != null) {
            var currentPlayingBitmap = _currentScreenState.value.currentPlayingBitmap
            _currentScreenState.update {
                if (isForPrevious == null) {
                    it.copy(
                        currentPlayingBitmap = bitmap.asImageBitmap(),
                    )
                } else if (isForPrevious) {
                    it.copy(
                        previousBitmap = bitmap.asImageBitmap(),
                    )
                } else {
                    it.copy(
                        currentBitmap = bitmap.asImageBitmap(),
                    )
                }
            }
            currentPlayingBitmap?.asAndroidBitmap()?.recycle()
            currentPlayingBitmap = null
        }
    }

    fun onGenerateClicked() {
        _currentScreenState.update {
            it.copy(
                isGeneratePanelOpened = true
            )
        }
    }

    fun closeGeneratePanel() {
        _currentScreenState.update {
            it.copy(
                isGeneratePanelOpened = false
            )
        }
    }

    private fun saveBitmap(name: Long, previousBitmap: ImageBitmap?, pathList: List<PathData>) {
        val uniqueNamePath: String = getFullPath(name)
        val path = File(uniqueNamePath)

        try {
            val fileOutputStream = FileOutputStream(path)
            var bitmap: Bitmap? = drawToBitmap(previousBitmap, pathList).asAndroidBitmap().apply {
                compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            }
            fileOutputStream.close()
            bitmap?.recycle()
            bitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawToBitmap(previousBitmap: ImageBitmap?, pathList: List<PathData>): ImageBitmap {
        val drawScope = CanvasDrawScope()

        val width = currentScreenState.value.frameWidth
        val height = currentScreenState.value.frameHeight

        val size = Size(width.toFloat(), height.toFloat())
        val bitmap = ImageBitmap(width, height)
        val canvas = androidx.compose.ui.graphics.Canvas(bitmap)

        drawScope.draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = size,
        ) {
            if (previousBitmap != null) {
                drawImage(previousBitmap)
            }
            pathList.forEach { pathData ->
                drawPath(
                    path = pathData.path,
                    color = pathData.color,
                    style = pathData.drawStyle,
                    blendMode = pathData.blendMode
                )
            }
        }
        return bitmap
    }

    private fun getFullPath(imageName: Long): String {
        val directory: File = getApplication<Application>().getDir("phenak", Context.MODE_PRIVATE)
        return String.format("%s/%s", directory, "${imageName}.png")
    }

    private fun loadBitmap(imageName: Long, isForPrevious: Boolean? = null) {
        val bitmap = try {
            decodeFile(getFullPath(imageName))
        } catch (e: Exception) {
            null
        }
        bitmapLoaded(bitmap, isForPrevious)
    }

    private fun deleteBitmap(imageName: Long): Boolean {
        val f = File(getFullPath(imageName))
        return f.delete()
    }
}
