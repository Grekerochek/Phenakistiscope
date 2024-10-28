package com.example.phenakistiscope

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun DrawScreen() {
    var tempPath = Path()
    var drawScreenState by remember { mutableStateOf(DrawScreenState()) }

    var pp = remember {
        mutableStateListOf(PathData())
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
                modifier = Modifier.clickable {
                    pp.removeAt(pp.size-1)
                    drawScreenState =
                        drawScreenState.copy(pathList = drawScreenState.pathList.dropLast(1))
                },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                contentDescription = "back",
                tint = if (drawScreenState.pathList.isEmpty()) {
                    colorResource(id = R.color.disabled_icon_color)
                } else {
                    colorResource(id = R.color.enabled_icon_color)
                },
            )
            Icon(
                modifier = Modifier.clickable { },
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_forward),
                contentDescription = "back",
                tint = Color.White,
            )
            Button(
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = { drawScreenState = drawScreenState.copy(currentColor = Color.Black) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Black button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = { drawScreenState = drawScreenState.copy(currentColor = Color.Red) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Black button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Green),
                onClick = { drawScreenState = drawScreenState.copy(currentColor = Color.Green) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Red button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Blue),
                onClick = { drawScreenState = drawScreenState.copy(currentColor = Color.Blue) },
                modifier = Modifier
                    .padding(3.dp)
                    .width(40.dp)
            ) {
                // Blue button
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = {
                    drawScreenState = drawScreenState.copy(pathList = emptyList())
                },
                modifier = Modifier.padding(3.dp)
            ) {
                Text(text = "Clear")
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = colorResource(id = R.color.canvas_color),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = {
                            tempPath = Path()
                        },
                        onDragEnd = {
                            pp.add(PathData(path = tempPath))
                            drawScreenState = drawScreenState.copy(
                                pathList = drawScreenState.pathList + PathData(
                                    path = tempPath,
                                    color = drawScreenState.currentColor,
                                    drawScreenState.currentDrawStyle
                                )
                            )
                        }
                    ) { change, dragAmount ->
                        // change.consume()
                        tempPath.moveTo(
                            change.position.x - dragAmount.x,
                            change.position.y - dragAmount.y,
                        )
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = drawScreenState.currentColor
                        )
                        Log.d("PROVERKA", "line")
                        tempPath.lineTo(
                            x = change.position.x,
                            y = change.position.y
                        )
                        // path = Path().apply { addPath(tempPath) }
                        /* drawScreenState =
                            drawScreenState.copy(path = Path().apply { addPath(tempPath) })*/
                        val currentPathList = if (drawScreenState.pathList.isNotEmpty()) {
                            drawScreenState.pathList.dropLast(1)
                        } else {
                            drawScreenState.pathList
                        }
                        if (pp.isNotEmpty()) {
                            pp.removeAt(pp.size - 1)
                        }
                        pp.add(PathData(path = tempPath))
                        drawScreenState = drawScreenState.copy(
                            pathList = currentPathList + PathData(
                                path = tempPath,
                                color = drawScreenState.currentColor,
                                drawScreenState.currentDrawStyle
                            )
                        )
                        // drawScreenState = drawScreenState.copy(lines = drawScreenState.lines + line)
                    }
                }
        ) {
            /*
            drawScreenState.lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strokeWidth,
                    cap = StrokeCap.Round
                )
            }*/
            pp.forEach { path ->
                drawPath(
                    path = path.path,
                    color = path.color,
                    style = path.drawStyle,
                )
            }
            /* drawScreenState.pathList.forEach { path ->
                 drawPath(
                     path = path.path,
                     color = path.color,
                     style = path.drawStyle,
                 )
             }*/
        }

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