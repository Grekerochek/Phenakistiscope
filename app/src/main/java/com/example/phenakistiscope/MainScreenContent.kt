package com.example.phenakistiscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.InstrumentState.DISABLED
import com.example.phenakistiscope.InstrumentState.ENABLED
import com.example.phenakistiscope.InstrumentState.SELECTED
import com.example.phenakistiscope.ui.theme.SpeedPanel
import com.example.phenakistiscope.ui.theme.StylePanel


@Composable
internal fun MainScreenContent(
    mainScreenState: MainScreenState,
    onAddFrameClicked: (Frame) -> Unit = {},
    onPlayClicked: () -> Unit = {},
    onPauseClicked: () -> Unit = {},
    onRemoveFrameClicked: () -> Unit = {},
    onInstrumentClicked: (Instrument) -> Unit = {},
    changeColor: (Color) -> Unit = {},
    changeStyle: (Float) -> Unit = {},
    changeSpeed: (Int) -> Unit = {},
    onGenerateSelected: (Int) -> Unit = {},
    onGenerateClicked: () -> Unit = {},
    onSizeChanged: (Int, Int) -> Unit = { _, _ -> },
    closeGeneratePanel: () -> Unit = {},
) {
    val pathList = remember {
        mutableStateListOf<PathData>()
    }

    val removedPathList = remember {
        mutableStateListOf<PathData>()
    }

    var isPalletOpened by rememberSaveable { mutableStateOf(false) }
    var isStylePanelOpened by rememberSaveable { mutableStateOf(false) }
    var isSpeedPanelOpened by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.main_color))
            .padding(horizontal = dimensionResource(id = R.dimen.main_dimen))
    ) {
        Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.main_dimen)))
        TopBar(
            pathList = pathList,
            removedPathList = removedPathList,
            mainScreenState = mainScreenState,
            onAddFrameClicked = onAddFrameClicked,
            onPauseClicked = onPauseClicked,
            onPlayClicked = onPlayClicked,
            onRemoveFrameClicked = onRemoveFrameClicked,
            onGenerateClicked = onGenerateClicked,
            openSpeedPanel = { isSpeedPanelOpened = !isSpeedPanelOpened },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(size = 20.dp))
                .background(
                    color = Color.White
                )
        ) {
            when (mainScreenState.currentScreen) {
                CurrentScreen.Edit -> {
                    if (mainScreenState.previousBitmap != null) {
                        PreviousFrame(mainScreenState.previousBitmap)
                    }
                    DrawCanvas(
                        modifier = Modifier
                            .fillMaxWidth(),
                        pathList = pathList,
                        removedPathList = removedPathList,
                        mainScreenState = mainScreenState,
                        onSizeChanged = onSizeChanged,
                    )
                }

                CurrentScreen.Play -> {
                    PlayScreenContent(mainScreenState.currentPlayingBitmap)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(size = 20.dp))
                    .paint(
                        painterResource(id = R.drawable.texture),
                        contentScale = ContentScale.FillBounds,
                        alpha = 0.1f, // used for better design
                    )
            )

            if (mainScreenState.currentScreen == CurrentScreen.Edit && isSpeedPanelOpened) {
                SpeedPanel(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(
                            top = dimensionResource(
                                id = R.dimen.main_dimen
                            )
                        ),
                    onSpeedSelected = { speed ->
                        changeSpeed(speed)
                        isSpeedPanelOpened = false
                    }
                )
            }

            if (mainScreenState.currentScreen == CurrentScreen.Edit && isPalletOpened) {
                Pallet(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = dimensionResource(
                                id = R.dimen.main_dimen
                            )
                        ),
                    onColorSelected = { color ->
                        changeColor(color)
                        isPalletOpened = false
                    }
                )
            }

            if (mainScreenState.currentScreen == CurrentScreen.Edit && isStylePanelOpened) {
                StylePanel(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = dimensionResource(
                                id = R.dimen.main_dimen
                            )
                        ),
                    currentColor = mainScreenState.currentColor,
                    onStyleSelected = { width ->
                        changeStyle(width)
                        isStylePanelOpened = false
                    }
                )
            }

            if (mainScreenState.isAddFrameEnabled && mainScreenState.isGeneratePanelOpened) {
                GeneratePanel(
                    mainScreenState = mainScreenState,
                    onGenerateSelected = { frameNumbers ->
                        onGenerateSelected(frameNumbers)
                    },
                    dismissClicked = {
                        if (!mainScreenState.isGenerating) {
                            closeGeneratePanel()
                        }
                    }
                )
            }
        }

        BottomBar(
            mainScreenState = mainScreenState,
            onInstrumentClicked = onInstrumentClicked,
            onPalletOpened = {
                if (isStylePanelOpened) {
                    isStylePanelOpened = false
                }
                isPalletOpened = !isPalletOpened
            },
            onStylePanelOpened = {
                if (isPalletOpened) {
                    isPalletOpened = false
                }
                isStylePanelOpened = !isStylePanelOpened
            }
        )
    }
}

@Composable
private fun TopBar(
    pathList: SnapshotStateList<PathData>,
    removedPathList: SnapshotStateList<PathData>,
    mainScreenState: MainScreenState,
    onAddFrameClicked: (Frame) -> Unit,
    onPauseClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onRemoveFrameClicked: () -> Unit,
    onGenerateClicked: () -> Unit,
    openSpeedPanel: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = colorResource(id = R.color.main_color)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.width(40.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val backButtonIsEnabled =
                mainScreenState.currentScreen == CurrentScreen.Edit && pathList.isNotEmpty()
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (backButtonIsEnabled) {
                            val removedPath = pathList.removeAt(pathList.size - 1)
                            removedPathList.add(removedPath)
                        }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                contentDescription = "back",
                tint = if (backButtonIsEnabled) {
                    colorResource(id = R.color.enabled_icon_color)
                } else {
                    colorResource(id = R.color.disabled_icon_color)
                },
            )
            val forwardButtonIsEnabled =
                mainScreenState.currentScreen == CurrentScreen.Edit && removedPathList.isNotEmpty()
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (forwardButtonIsEnabled) {
                            val restoredPath = removedPathList.removeAt(removedPathList.size - 1)
                            pathList.add(restoredPath)
                        }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_forward),
                contentDescription = "forward",
                tint = if (forwardButtonIsEnabled) {
                    colorResource(id = R.color.enabled_icon_color)
                } else {
                    colorResource(id = R.color.disabled_icon_color)
                },
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

            Icon(
                modifier = Modifier
                    .size(26.dp)
                    .padding(all = 1.dp).clip(CircleShape)
                    .clickable { onPlayClicked() },
                imageVector = ImageVector.vectorResource(id = R.drawable.play),
                contentDescription = "play",
                tint = colorResource(
                    id = when (mainScreenState.playState) {
                        SELECTED -> R.color.selected_icon_color
                        ENABLED -> R.color.enabled_icon_color
                        DISABLED -> R.color.disabled_icon_color
                    }
                ),
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onPauseClicked() },
                imageVector = ImageVector.vectorResource(id = R.drawable.pause),
                contentDescription = "pause",
                tint = colorResource(
                    id = if (mainScreenState.isPauseEnabled) {
                        R.color.enabled_icon_color
                    } else {
                        R.color.disabled_icon_color
                    }
                )
            )
            Text(
                text = "Speed",
                color = colorResource(
                    id =
                    if (mainScreenState.currentScreen == CurrentScreen.Edit) {
                        R.color.enabled_icon_color
                    } else {
                        R.color.disabled_icon_color
                    }
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { openSpeedPanel() }
                    .padding(horizontal = 4.dp),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (mainScreenState.isAddFrameEnabled) {
                            onAddFrameClicked(
                                Frame(
                                    pathList = pathList.toList(),
                                    bitmap = mainScreenState.currentBitmap,
                                )
                            )
                            pathList.clear()
                        }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.add_frame),
                contentDescription = "new frame",
                tint = colorResource(
                    id = if (mainScreenState.isAddFrameEnabled) {
                        R.color.enabled_icon_color
                    } else {
                        R.color.disabled_icon_color
                    }
                ),
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onRemoveFrameClicked()
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.bin),
                contentDescription = "remove frame",
                tint = colorResource(
                    id = if (mainScreenState.isBinEnabled) {
                        R.color.enabled_icon_color
                    } else {
                        R.color.disabled_icon_color
                    }
                ),
            )
        }

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .clickable {
                    if (mainScreenState.isAddFrameEnabled) {
                        onGenerateClicked()
                    }
                }
                .padding(3.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ge),
            contentDescription = "generate frame",
            tint = colorResource(
                id = if (mainScreenState.isAddFrameEnabled) {
                    R.color.enabled_icon_color
                } else {
                    R.color.disabled_icon_color
                }
            ),
        )
    }
}

@Composable
private fun BottomBar(
    mainScreenState: MainScreenState,
    onInstrumentClicked: (Instrument) -> Unit,
    onPalletOpened: () -> Unit,
    onStylePanelOpened: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = colorResource(id = R.color.main_color)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onInstrumentClicked(Instrument.Pencil) },
            imageVector = ImageVector.vectorResource(id = R.drawable.pencil),
            contentDescription = "pencil",
            tint = colorResource(
                id = when (mainScreenState.pencilState) {
                    SELECTED -> R.color.selected_icon_color
                    ENABLED -> R.color.enabled_icon_color
                    DISABLED -> R.color.disabled_icon_color
                }
            ),
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onInstrumentClicked(Instrument.Eraser) },
            imageVector = ImageVector.vectorResource(id = R.drawable.erase),
            contentDescription = "erase",
            tint = colorResource(
                id = when (mainScreenState.eraserState) {
                    SELECTED -> R.color.selected_icon_color
                    ENABLED -> R.color.enabled_icon_color
                    DISABLED -> R.color.disabled_icon_color
                }
            ),
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

        // open select color and style panels
        if (mainScreenState.currentScreen == CurrentScreen.Edit) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, colorResource(id = R.color.selected_color), CircleShape)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (mainScreenState.currentInstrument == Instrument.Eraser) {
                            mainScreenState.previousColor
                        } else {
                            mainScreenState.currentColor
                        }
                    )
                    .clickable { onPalletOpened() }
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onStylePanelOpened()
                    }
                    .padding(all = 2.dp),
                text = "St",
                color = colorResource(id = R.color.enabled_icon_color)
            )
        }
    }
}

@Composable
private fun DrawCanvas(
    modifier: Modifier,
    pathList: SnapshotStateList<PathData>,
    removedPathList: SnapshotStateList<PathData>,
    mainScreenState: MainScreenState,
    onSizeChanged: (Int, Int) -> Unit,
) {
    var tempPath = Path()

    Box(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.canvas_color).copy(alpha = 0.1f),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .pointerInput(mainScreenState) {
                detectDragGestures(
                    onDragStart = {
                        tempPath = Path()

                        pathList.add(
                            PathData(
                                path = tempPath,
                                color = mainScreenState.currentColor,
                                drawStyle = mainScreenState.currentDrawStyle,
                                blendMode = mainScreenState.blendMode,
                            )
                        )
                        removedPathList.clear()
                    },
                ) { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y,
                    )
                    tempPath.lineTo(
                        x = change.position.x,
                        y = change.position.y
                    )
                    if (pathList.isNotEmpty()) {
                        pathList.removeAt(pathList.size - 1)
                    }
                    pathList.add(
                        PathData(
                            path = tempPath,
                            color = mainScreenState.currentColor,
                            drawStyle = mainScreenState.currentDrawStyle,
                            blendMode = mainScreenState.blendMode,
                        )
                    )
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer(alpha = 0.99F)
                .onSizeChanged { size -> onSizeChanged(size.width, size.height) }
        ) {
            if (mainScreenState.currentBitmap != null) {
                drawImage(mainScreenState.currentBitmap)
            }
            pathList.forEach { pathData ->
                drawPath(
                    path = pathData.path,
                    color = pathData.color,
                    style = pathData.drawStyle,
                    blendMode = pathData.blendMode,
                )
            }
        }
    }
}

@Composable
private fun PreviousFrame(imageBitmap: ImageBitmap) {
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer(alpha = 0.99F)
        ) {
            drawImage(imageBitmap)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = 20.dp))
                .background(
                    color = Color.White.copy(alpha = 0.4f) // used for better design
                )
        )
    }
}
