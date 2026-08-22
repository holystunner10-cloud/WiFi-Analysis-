package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SignalHistoryPoint
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueSoft

@Composable
fun SignalTimeGraph(
    history: List<SignalHistoryPoint>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("signal_time_graph_card"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Signal Stability (Live)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(VibrantBlue, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Rolling (dBm)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw grid lines (-40 dBm, -60 dBm, -80 dBm, -100 dBm)
                    val dbmLevels = listOf(-40, -60, -80, -100)
                    for (dbm in dbmLevels) {
                        val y = h * (dbm - (-30f)) / (-100f - (-30f))
                        drawLine(
                            color = Slate200,
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (history.size < 2) return@Canvas

                    val path = Path()
                    val fillPath = Path()

                    val stepX = w / (history.size - 1).coerceAtLeast(1)

                    history.forEachIndexed { index, point ->
                        val x = index * stepX
                        // Normalize RSSI from -30 (top 0f) to -100 (bottom h)
                        val norm = ((point.rssi - (-30f)) / (-100f - (-30f))).coerceIn(0f, 1f)
                        val y = norm * h

                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, h)
                            fillPath.lineTo(x, y)
                        } else {
                            val prevX = (index - 1) * stepX
                            val prevNorm = ((history[index - 1].rssi - (-30f)) / (-100f - (-30f))).coerceIn(0f, 1f)
                            val prevY = prevNorm * h

                            // Smooth cubic curve
                            val cpx1 = (prevX + x) / 2
                            val cpy1 = prevY
                            val cpx2 = (prevX + x) / 2
                            val cpy2 = y
                            path.cubicTo(cpx1, cpy1, cpx2, cpy2, x, y)
                            fillPath.cubicTo(cpx1, cpy1, cpx2, cpy2, x, y)
                        }

                        if (index == history.lastIndex) {
                            fillPath.lineTo(x, h)
                            fillPath.close()
                        }
                    }

                    // Draw area gradient fill
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                VibrantBlue.copy(alpha = 0.25f),
                                VibrantBlue.copy(alpha = 0.01f)
                            ),
                            startY = 0f,
                            endY = h
                        )
                    )

                    // Draw line stroke
                    drawPath(
                        path = path,
                        color = VibrantBlue,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw current tip dot
                    val lastPoint = history.last()
                    val lastNorm = ((lastPoint.rssi - (-30f)) / (-100f - (-30f))).coerceIn(0f, 1f)
                    val lastX = (history.size - 1) * stepX
                    val lastY = lastNorm * h

                    drawCircle(
                        color = VibrantBlue.copy(alpha = 0.25f),
                        radius = 8.dp.toPx(),
                        center = Offset(lastX, lastY)
                    )
                    drawCircle(
                        color = VibrantBlue,
                        radius = 4.dp.toPx(),
                        center = Offset(lastX, lastY)
                    )
                }
            }

            // Legend labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("-30 dBm (Max)", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate500)
                Text("-60 dBm", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate500)
                Text("-100 dBm (Min)", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate500)
            }
        }
    }
}

