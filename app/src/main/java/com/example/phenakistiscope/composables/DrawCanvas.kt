package com.example.phenakistiscope.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.MainScreenState
import com.example.phenakistiscope.PathData
import com.example.phenakistiscope.R

@Composable
internal fun DrawCanvas(
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
