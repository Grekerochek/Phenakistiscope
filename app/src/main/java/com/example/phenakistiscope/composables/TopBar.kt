package com.example.phenakistiscope.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.CurrentScreen
import com.example.phenakistiscope.Frame
import com.example.phenakistiscope.InstrumentState.DISABLED
import com.example.phenakistiscope.InstrumentState.ENABLED
import com.example.phenakistiscope.InstrumentState.SELECTED
import com.example.phenakistiscope.MainScreenState
import com.example.phenakistiscope.PathData
import com.example.phenakistiscope.R

@Composable
internal fun TopBar(
    pathList: SnapshotStateList<PathData>,
    removedPathList: SnapshotStateList<PathData>,
    mainScreenState: MainScreenState,
    onAddFrameClicked: (Frame) -> Unit,
    onCopyFrameClicked: () -> Unit,
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
                    .padding(all = 1.dp)
                    .clip(CircleShape)
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
                        pathList.clear()
                        removedPathList.clear()
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (mainScreenState.isCopyFrameEnabled) {
                            onCopyFrameClicked()
                        }
                    },
                text = "C",
                color = colorResource(
                    id = if (mainScreenState.isCopyFrameEnabled) {
                        R.color.enabled_icon_color
                    } else {
                        R.color.disabled_icon_color
                    }
                )
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
            imageVector = ImageVector.vectorResource(id = R.drawable.generate),
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
