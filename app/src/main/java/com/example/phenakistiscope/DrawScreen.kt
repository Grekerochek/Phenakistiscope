package com.example.phenakistiscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun DrawScreen() {
    val drawScreenState = remember { mutableStateOf(DrawScreenState()) }

    val pathList = remember {
        mutableStateListOf<PathData>()
    }

    val removedPathList = remember {
        mutableStateListOf<PathData>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.main_color))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = colorResource(id = R.color.main_color)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (pathList.isNotEmpty()) {
                            val removedPath = pathList.removeAt(pathList.size - 1)
                            removedPathList.add(removedPath)
                        }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                contentDescription = "back",
                tint = if (pathList.isEmpty()) {
                    colorResource(id = R.color.disabled_icon_color)
                } else {
                    colorResource(id = R.color.enabled_icon_color)
                },
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (removedPathList.isNotEmpty()) {
                            val restoredPath = removedPathList.removeAt(removedPathList.size - 1)
                            pathList.add(restoredPath)
                        }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_forward),
                contentDescription = "forward",
                tint = if (removedPathList.isEmpty()) {
                    colorResource(id = R.color.disabled_icon_color)
                } else {
                    colorResource(id = R.color.enabled_icon_color)
                },
            )
            Button(
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = { drawScreenState.value = drawScreenState.value.copy(currentColor = Color.Black) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Black button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = { drawScreenState.value = drawScreenState.value.copy(currentColor = Color.Red) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Black button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Green),
                onClick = { drawScreenState.value = drawScreenState.value.copy(currentColor = Color.Green) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Red button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Blue),
                onClick = { drawScreenState.value = drawScreenState.value.copy(currentColor = Color.Blue) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Blue button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = {
                    pathList.clear()
                },
                modifier = Modifier.padding(3.dp)
            ) {
                Text(text = "Clear")
            }
        }
        DrawCanvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            pathList = pathList,
            removedPathList = removedPathList,
            drawScreenState = drawScreenState,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = colorResource(id = R.color.main_color)),
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }
}

@Composable
private fun DrawCanvas(
    modifier: Modifier,
    pathList: SnapshotStateList<PathData>,
    removedPathList: SnapshotStateList<PathData>,
    drawScreenState: MutableState<DrawScreenState>,
) {
    var tempPath = Path()

    Box(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.canvas_color),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        tempPath = Path()
                        pathList.add(
                            PathData(
                                path = tempPath,
                                color = drawScreenState.value.currentColor,
                                drawStyle = drawScreenState.value.currentDrawStyle,
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
                            color = drawScreenState.value.currentColor,
                            drawStyle = drawScreenState.value.currentDrawStyle,
                        )
                    )
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize().clipToBounds()) {
            pathList.forEach { path ->
                drawPath(
                    path = path.path,
                    color = path.color,
                    style = path.drawStyle,
                )
            }
        }
    }
}
