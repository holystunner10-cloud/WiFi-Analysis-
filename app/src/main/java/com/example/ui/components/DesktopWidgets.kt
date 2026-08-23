package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DesktopWidgets(
    cpuUsage: Float,
    ramUsageMb: Int,
    activeWindowsCount: Int,
    onOpenCopilot: () -> Unit,
    onOpenTaskManager: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(getFormattedTime()) }
    var currentDate by remember { mutableStateOf(getFormattedDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getFormattedTime()
            currentDate = getFormattedDate()
            delay(1000)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Clock & Date Display
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = currentTime,
                fontSize = 44.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Text(
                text = currentDate,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF93C5FD),
                letterSpacing = 0.5.sp
            )
        }

        // Hardware Gauges Widget
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x550F172A))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(20.dp))
                .clickable { onOpenTaskManager() }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // CPU
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "CPU",
                    tint = if (cpuUsage > 75f) Color(0xFFEF4444) else Color(0xFF60A5FA),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${cpuUsage.toInt()}% CPU",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // RAM
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "RAM",
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${ramUsageMb} MB",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Active Tasks
            Box(
                modifier = Modifier
                    .background(Color(0x333B82F6), RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$activeWindowsCount Tasks",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF93C5FD)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AI Copilot Quick Action Bar
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0x991E1B4B), Color(0x990F172A))
                    )
                )
                .border(1.dp, Color(0x66818CF8), RoundedCornerShape(24.dp))
                .clickable { onOpenCopilot() }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFFEC4899))),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Copilot",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Ask Holy Stunner AI or execute command...",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFCBD5E1)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF818CF8),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun getFormattedTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}

private fun getFormattedDate(): String {
    return SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
}
