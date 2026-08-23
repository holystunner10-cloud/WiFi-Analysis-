package com.example.ui.apps

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DrawPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun PaintStudioApp(modifier: Modifier = Modifier) {
    val paths = remember { mutableStateListOf<DrawPath>() }
    var currentPoints = remember { mutableStateListOf<Offset>() }
    var currentColor by remember { mutableStateOf(Color(0xFF3B82F6)) }
    var strokeWidth by remember { mutableStateOf(6f) }
    var isEraser by remember { mutableStateOf(false) }

    val colors = listOf(
        Color.White,
        Color(0xFFEF4444),
        Color(0xFFF97316),
        Color(0xFFFACC15),
        Color(0xFF22C55E),
        Color(0xFF3B82F6),
        Color(0xFFA855F7),
        Color(0xFFEC4899),
        Color(0xFF0F172A)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Paint Top Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Color Palette
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                colors.forEach { col ->
                    val isSelected = !isEraser && currentColor == col
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(col)
                            .border(
                                2.dp,
                                if (isSelected) Color(0xFF60A5FA) else Color(0x33FFFFFF),
                                CircleShape
                            )
                            .clickable {
                                isEraser = false
                                currentColor = col
                            }
                    )
                }
            }

            // Eraser, Undo, Clear
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Eraser",
                    fontSize = 11.sp,
                    color = if (isEraser) Color(0xFFEF4444) else Color(0xFF94A3B8),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isEraser) Color(0x33EF4444) else Color.Transparent)
                        .clickable { isEraser = !isEraser }
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )

                IconButton(
                    onClick = {
                        if (paths.isNotEmpty()) paths.removeAt(paths.size - 1)
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { paths.clear() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Clear",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Canvas Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF0F172A))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPoints.clear()
                            currentPoints.add(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentPoints.add(change.position)
                        },
                        onDragEnd = {
                            if (currentPoints.isNotEmpty()) {
                                paths.add(
                                    DrawPath(
                                        points = currentPoints.toList(),
                                        color = if (isEraser) Color(0xFF0F172A) else currentColor,
                                        strokeWidth = if (isEraser) strokeWidth * 3f else strokeWidth
                                    )
                                )
                                currentPoints.clear()
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                paths.forEach { drawPath ->
                    for (i in 0 until drawPath.points.size - 1) {
                        drawLine(
                            color = drawPath.color,
                            start = drawPath.points[i],
                            end = drawPath.points[i + 1],
                            strokeWidth = drawPath.strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Current stroke in progress
                for (i in 0 until currentPoints.size - 1) {
                    drawLine(
                        color = if (isEraser) Color(0xFF0F172A) else currentColor,
                        start = currentPoints[i],
                        end = currentPoints[i + 1],
                        strokeWidth = if (isEraser) strokeWidth * 3f else strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
