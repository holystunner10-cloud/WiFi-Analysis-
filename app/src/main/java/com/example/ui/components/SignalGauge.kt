package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SignalQuality
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.VibrantRose
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SignalGauge(
    rssi: Int,
    percentage: Int,
    quality: SignalQuality,
    modifier: Modifier = Modifier
) {
    // RSSI spans from -100 (0.0) to -30 (1.0)
    val normalized = ((rssi + 100).toFloat() / 70f).coerceIn(0f, 1f)
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(normalized) {
        animatedProgress.animateTo(
            targetValue = normalized,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    val primaryColor = when (quality) {
        SignalQuality.EXCELLENT -> VibrantBlue
        SignalQuality.GOOD -> VibrantGreen
        SignalQuality.FAIR -> VibrantAmber
        SignalQuality.POOR -> VibrantOrange
        SignalQuality.VERY_POOR -> VibrantRose
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("signal_gauge_card"),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp, 140.dp)
                    .testTag("gauge_canvas_box"),
                contentAlignment = Alignment.BottomCenter
            ) {
                Canvas(modifier = Modifier.size(220.dp)) {
                    val strokeWidth = 16.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth * 2, size.height - strokeWidth * 2)
                    val topLeft = Offset(strokeWidth, strokeWidth)
                    val startAngle = 150f
                    val sweepAngle = 240f

                    // Background Track in clean Slate 200
                    drawArc(
                        color = Color(0xFFE2E8F0),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Vibrant Gradient Arc
                    val currentSweep = sweepAngle * animatedProgress.value
                    if (currentSweep > 0) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                0.0f to VibrantRose,
                                0.35f to VibrantAmber,
                                0.7f to VibrantGreen,
                                1.0f to VibrantBlue
                            ),
                            startAngle = startAngle,
                            sweepAngle = currentSweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Ticks around the arc
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = (size.width - strokeWidth * 2) / 2
                    val tickSteps = 8
                    for (i in 0..tickSteps) {
                        val angleDeg = startAngle + (sweepAngle / tickSteps) * i
                        val angleRad = Math.toRadians(angleDeg.toDouble())
                        val tickStart = Offset(
                            (center.x + (radius - 10.dp.toPx()) * cos(angleRad)).toFloat(),
                            (center.y + (radius - 10.dp.toPx()) * sin(angleRad)).toFloat()
                        )
                        val tickEnd = Offset(
                            (center.x + (radius - 4.dp.toPx()) * cos(angleRad)).toFloat(),
                            (center.y + (radius - 4.dp.toPx()) * sin(angleRad)).toFloat()
                        )
                        drawContext.canvas.drawLine(
                            tickStart,
                            tickEnd,
                            androidx.compose.ui.graphics.Paint().apply {
                                color = Slate300
                                this.strokeWidth = 2.dp.toPx()
                            }
                        )
                    }
                }

                // Center Numerical Value
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$rssi",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "dBm",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate500,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status Quality Pill & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = primaryColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(primaryColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = quality.label.uppercase(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Signal Icon",
                        tint = primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$percentage%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
            }
        }
    }
}
