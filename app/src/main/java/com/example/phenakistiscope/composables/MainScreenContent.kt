package com.example.phenakistiscope.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.CurrentScreen
import com.example.phenakistiscope.Frame
import com.example.phenakistiscope.Instrument
import com.example.phenakistiscope.MainScreenState
import com.example.phenakistiscope.PathData
import com.example.phenakistiscope.R
import com.example.phenakistiscope.composables.panels.GeneratePanel
import com.example.phenakistiscope.composables.panels.Pallet
import com.example.phenakistiscope.composables.panels.SpeedPanel
import com.example.phenakistiscope.composables.panels.StylePanel

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
    onCopyFrameClicked: () -> Unit = {},
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
            onCopyFrameClicked = onCopyFrameClicked,
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
