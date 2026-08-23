package com.example.ui.components

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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun SystemTrayFlyout(
    isOpen: Boolean,
    wifiEnabled: Boolean,
    bluetoothEnabled: Boolean,
    airplaneMode: Boolean,
    volumeLevel: Float,
    brightnessLevel: Float,
    onToggleWifi: () -> Unit,
    onToggleBluetooth: () -> Unit,
    onToggleAirplane: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTaskManager: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 60.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Surface(
            modifier = Modifier
                .width(310.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(16.dp))
                .testTag("system_tray_surface"),
            color = Color(0xFA0B132B),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Quick Settings",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Toggle Buttons (2x2 Grid)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToggleTile(
                        label = "Wi-Fi",
                        sublabel = if (wifiEnabled) "Connected" else "Off",
                        icon = if (wifiEnabled) Icons.Default.Wifi else Icons.Default.WifiOff,
                        isActive = wifiEnabled,
                        onClick = onToggleWifi,
                        modifier = Modifier.weight(1f)
                    )
                    QuickToggleTile(
                        label = "Bluetooth",
                        sublabel = if (bluetoothEnabled) "Ready" else "Off",
                        icon = Icons.Default.Bluetooth,
                        isActive = bluetoothEnabled,
                        onClick = onToggleBluetooth,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToggleTile(
                        label = "Airplane",
                        sublabel = if (airplaneMode) "Enabled" else "Off",
                        icon = Icons.Default.AirplanemodeActive,
                        isActive = airplaneMode,
                        onClick = onToggleAirplane,
                        modifier = Modifier.weight(1f)
                    )
                    QuickToggleTile(
                        label = "Task Mgr",
                        sublabel = "Performance",
                        icon = Icons.Default.Speed,
                        isActive = true,
                        onClick = {
                            onOpenTaskManager()
                            onClose()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Volume Slider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Volume",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = volumeLevel,
                        onValueChange = onVolumeChange,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF3B82F6),
                            activeTrackColor = Color(0xFF60A5FA),
                            inactiveTrackColor = Color(0x3360A5FA)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${(volumeLevel * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8)
                    )
                }

                // Brightness Slider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.BrightnessMedium,
                        contentDescription = "Brightness",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = brightnessLevel,
                        onValueChange = onBrightnessChange,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFF59E0B),
                            activeTrackColor = Color(0xFFFBBF24),
                            inactiveTrackColor = Color(0x33F59E0B)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${(brightnessLevel * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0x2260A5FA))
                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BatteryFull,
                            contentDescription = "Battery",
                            tint = Color(0xFF4ADE80),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "88% Battery",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onOpenSettings()
                                onClose()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "All Settings",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickToggleTile(
    label: String,
    sublabel: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) Color(0xFF2563EB) else Color(0x221E293B))
            .border(
                1.dp,
                if (isActive) Color(0xFF60A5FA) else Color(0x2260A5FA),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color.White else Color(0xFF94A3B8),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = sublabel,
                fontSize = 10.sp,
                color = if (isActive) Color(0xFFDBEAFE) else Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun CalendarFlyout(
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    var currentTime by remember { mutableStateOf(getDetailedTime()) }
    var currentDate by remember { mutableStateOf(getDetailedDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getDetailedTime()
            currentDate = getDetailedDate()
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 60.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(16.dp))
                .testTag("calendar_flyout_surface"),
            color = Color(0xFA0B132B),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = currentTime,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentDate,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF60A5FA)
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0x2260A5FA))
                Spacer(modifier = Modifier.height(14.dp))

                // Monthly Mini Calendar Grid
                CalendarMonthGrid()
            }
        }
    }
}

@Composable
fun CalendarMonthGrid() {
    val cal = Calendar.getInstance()
    val today = cal.get(Calendar.DAY_OF_MONTH)
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = monthName,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Day of week headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Simple representative month grid
        val daysInMonth = (1..31).toList()
        val weeks = daysInMonth.chunked(7)

        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { day ->
                    val isToday = day == today
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isToday) Color(0xFF2563EB) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isToday) Color(0xFF93C5FD) else Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$day",
                            fontSize = 11.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) Color.White else Color(0xFFCBD5E1)
                        )
                    }
                }
                // Fill remaining empty space for partial week
                repeat(7 - week.size) {
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }
        }
    }
}

@Composable
fun DesktopContextMenu(
    isOpen: Boolean,
    onNewFolder: () -> Unit,
    onNewFile: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenCopilot: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 60.dp, top = 120.dp)
    ) {
        Surface(
            modifier = Modifier
                .width(220.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(12.dp)),
            color = Color(0xFA0B132B),
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                ContextMenuItem(
                    label = "Gemini AI Copilot",
                    icon = Icons.Default.AutoAwesome,
                    tint = Color(0xFFEC4899),
                    onClick = {
                        onOpenCopilot()
                        onClose()
                    }
                )
                ContextMenuItem(
                    label = "New Folder",
                    icon = Icons.Default.CreateNewFolder,
                    tint = Color(0xFFF59E0B),
                    onClick = {
                        onNewFolder()
                        onClose()
                    }
                )
                ContextMenuItem(
                    label = "New Text Document",
                    icon = Icons.Default.Description,
                    tint = Color(0xFF60A5FA),
                    onClick = {
                        onNewFile()
                        onClose()
                    }
                )
                ContextMenuItem(
                    label = "Open Terminal Here",
                    icon = Icons.Default.Terminal,
                    tint = Color(0xFF4ADE80),
                    onClick = {
                        onOpenTerminal()
                        onClose()
                    }
                )
                HorizontalDivider(color = Color(0x2260A5FA), modifier = Modifier.padding(vertical = 4.dp))
                ContextMenuItem(
                    label = "PC App Store",
                    icon = Icons.Default.Store,
                    tint = Color(0xFF38BDF8),
                    onClick = {
                        onOpenStore()
                        onClose()
                    }
                )
                ContextMenuItem(
                    label = "Personalize Wallpaper",
                    icon = Icons.Default.Palette,
                    tint = Color(0xFFA78BFA),
                    onClick = {
                        onOpenSettings()
                        onClose()
                    }
                )
            }
        }
    }
}

@Composable
fun ContextMenuItem(
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

private fun getDetailedTime(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}

private fun getDetailedDate(): String {
    return SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
}
