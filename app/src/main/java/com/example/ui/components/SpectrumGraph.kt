package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScanNetworkItem
import com.example.data.model.WifiBand
import com.example.ui.theme.ChannelColors
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueDark

@Composable
fun SpectrumGraph(
    band: WifiBand,
    networks: List<ScanNetworkItem>,
    modifier: Modifier = Modifier
) {
    val is24G = band == WifiBand.BAND_2_4_GHZ
    val channelList = if (is24G) {
        (1..14).toList()
    } else {
        listOf(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)
    }

    val bandNetworks = networks.filter { it.band == band }
    val canvasWidth = if (is24G) 520.dp else 1100.dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("spectrum_graph_card"),
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
                Column {
                    Text(
                        text = if (is24G) "2.4 GHz Spectrum (CH 1 - 14)" else "5 GHz Spectrum (UNII 1-3)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "${bandNetworks.size} Active APs detected",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate500
                    )
                }
                Text(
                    text = "Scroll ⇄",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantBlue
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontally Scrollable Spectrum Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Canvas(
                    modifier = Modifier
                        .width(canvasWidth)
                        .height(230.dp)
                        .testTag("spectrum_canvas")
                ) {
                    val w = size.width
                    val h = size.height - 30.dp.toPx() // Bottom padding for channel labels
                    val channelSlotWidth = w / channelList.size

                    // Draw dBm Horizontal Grid Lines (-30, -50, -70, -90 dBm)
                    val dbmLevels = listOf(-30, -50, -70, -90)
                    for (dbm in dbmLevels) {
                        val y = h * (dbm - (-25f)) / (-100f - (-25f))
                        drawLine(
                            color = Slate200,
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawContext.canvas.nativeCanvas.drawText(
                            "$dbm dBm",
                            12.dp.toPx(),
                            y - 4.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.rgb(148, 163, 184) // Slate 400
                                textSize = 24f
                                isAntiAlias = true
                            }
                        )
                    }

                    // Draw Vertical Channel Dividers & Labels
                    channelList.forEachIndexed { index, ch ->
                        val centerX = index * channelSlotWidth + channelSlotWidth / 2

                        // Vertical guide line
                        drawLine(
                            color = Slate200.copy(alpha = 0.6f),
                            start = Offset(centerX, 0f),
                            end = Offset(centerX, h),
                            strokeWidth = 1.dp.toPx()
                        )

                        // Channel text label at bottom
                        drawContext.canvas.nativeCanvas.drawText(
                            "CH $ch",
                            centerX,
                            size.height - 6.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = if (ch in listOf(1, 6, 11) && is24G) {
                                    android.graphics.Color.rgb(37, 99, 235) // Vibrant Blue
                                } else {
                                    android.graphics.Color.rgb(71, 85, 105) // Slate 600
                                }
                                textSize = 28f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = (ch in listOf(1, 6, 11) && is24G)
                                isAntiAlias = true
                            }
                        )
                    }

                    // Draw AP Parabolas (Curves)
                    bandNetworks.forEachIndexed { idx, net ->
                        val color = ChannelColors[idx % ChannelColors.size]
                        val channelIndex = channelList.indexOf(net.channel)
                        if (channelIndex >= 0) {
                            val centerX = channelIndex * channelSlotWidth + channelSlotWidth / 2
                            // Peak Y position based on RSSI (-25 to -100)
                            val normY = ((net.rssi - (-25f)) / (-100f - (-25f))).coerceIn(0.05f, 0.95f)
                            val peakY = normY * h

                            // Width of parabola depends on channel width (20/40/80 MHz)
                            val spreadFactor = when (net.channelWidthMhz) {
                                40 -> 2.4f
                                80 -> 4.2f
                                160 -> 7.0f
                                else -> 1.5f
                            }
                            val halfSpread = (channelSlotWidth * spreadFactor)

                            drawSpectrumCurve(
                                centerX = centerX,
                                peakY = peakY,
                                baseBottomY = h,
                                halfWidth = halfSpread,
                                color = color,
                                ssid = net.ssid,
                                rssi = net.rssi,
                                isConnected = net.isConnected
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawSpectrumCurve(
    centerX: Float,
    peakY: Float,
    baseBottomY: Float,
    halfWidth: Float,
    color: Color,
    ssid: String,
    rssi: Int,
    isConnected: Boolean
) {
    val leftX = centerX - halfWidth
    val rightX = centerX + halfWidth

    val path = Path().apply {
        moveTo(leftX, baseBottomY)
        // Smooth bell curve with control points
        cubicTo(
            centerX - halfWidth * 0.45f, baseBottomY,
            centerX - halfWidth * 0.35f, peakY,
            centerX, peakY
        )
        cubicTo(
            centerX + halfWidth * 0.35f, peakY,
            centerX + halfWidth * 0.45f, baseBottomY,
            rightX, baseBottomY
        )
        close()
    }

    // Fill semi-transparent gradient under curve
    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = if (isConnected) 0.40f else 0.22f),
                color.copy(alpha = 0.02f)
            ),
            startY = peakY,
            endY = baseBottomY
        )
    )

    // Draw solid stroke outline
    drawPath(
        path = path,
        color = if (isConnected) VibrantBlueDark else color,
        style = Stroke(
            width = if (isConnected) 3.5.dp.toPx() else 2.5.dp.toPx()
        )
    )

    // Peak Label: SSID and RSSI
    val displayText = if (ssid.length > 14) ssid.take(12) + ".." else ssid
    val labelPaint = android.graphics.Paint().apply {
        this.color = if (isConnected) android.graphics.Color.rgb(15, 23, 42) else android.graphics.Color.rgb(51, 65, 85)
        textSize = 26f
        textAlign = android.graphics.Paint.Align.CENTER
        isFakeBoldText = isConnected
        isAntiAlias = true
    }

    drawContext.canvas.nativeCanvas.drawText(
        "$displayText ($rssi)",
        centerX,
        (peakY - 8.dp.toPx()).coerceAtLeast(20.dp.toPx()),
        labelPaint
    )
}

