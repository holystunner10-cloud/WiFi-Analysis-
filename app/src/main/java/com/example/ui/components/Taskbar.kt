package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PcApp
import com.example.data.model.WindowState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DesktopTaskbar(
    isStartMenuOpen: Boolean,
    isSystemTrayOpen: Boolean,
    isCalendarOpen: Boolean,
    pinnedApps: List<PcApp>,
    openWindows: List<WindowState>,
    activeWindowId: String?,
    wifiEnabled: Boolean,
    onToggleStartMenu: () -> Unit,
    onToggleSystemTray: () -> Unit,
    onToggleCalendar: () -> Unit,
    onOpenApp: (String) -> Unit,
    onFocusOrMinimizeWindow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(getFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getFormattedTime()
            delay(1000)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(52.dp)
            .shadow(elevation = 12.dp)
            .testTag("desktop_taskbar"),
        color = Color(0xDD0B132B),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .border(
                    width = 0.5.dp,
                    color = Color(0x3360A5FA),
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left / Center Section: Start Button & App Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                // START BUTTON
                StartButton(
                    isOpen = isStartMenuOpen,
                    onClick = onToggleStartMenu
                )

                Spacer(modifier = Modifier.width(6.dp))

                // SEARCH / COPILOT BAR BUTTON
                TaskbarSearchPill(
                    onClick = {
                        onToggleStartMenu()
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // PINNED & OPEN APPS LIST
                val displayedAppIds = mutableSetOf<String>()
                val taskbarItems = mutableListOf<TaskbarAppEntry>()

                // Add pinned apps
                pinnedApps.filter { it.isPinnedTaskbar && it.isInstalled }.forEach { app ->
                    val openWin = openWindows.find { it.appId == app.id }
                    taskbarItems.add(
                        TaskbarAppEntry(
                            appId = app.id,
                            name = app.name,
                            iconName = app.iconName,
                            isOpen = openWin != null,
                            isActive = openWin?.id == activeWindowId && openWin?.isMinimized == false,
                            windowId = openWin?.id
                        )
                    )
                    displayedAppIds.add(app.id)
                }

                // Add unpinned currently open windows
                openWindows.forEach { win ->
                    if (!displayedAppIds.contains(win.appId)) {
                        taskbarItems.add(
                            TaskbarAppEntry(
                                appId = win.appId,
                                name = win.title,
                                iconName = win.iconName,
                                isOpen = true,
                                isActive = win.id == activeWindowId && !win.isMinimized,
                                windowId = win.id
                            )
                        )
                        displayedAppIds.add(win.appId)
                    }
                }

                taskbarItems.forEach { item ->
                    TaskbarAppIcon(
                        item = item,
                        onClick = {
                            if (item.windowId != null) {
                                onFocusOrMinimizeWindow(item.windowId)
                            } else {
                                onOpenApp(item.appId)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            // Right Section: System Tray & Clock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                // Quick Settings Tray Button (Wi-Fi, Volume, Battery)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSystemTrayOpen) Color(0x443B82F6) else Color(0x22FFFFFF))
                        .clickable { onToggleSystemTray() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("btn_system_tray"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (wifiEnabled) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = "Wi-Fi",
                        tint = if (wifiEnabled) Color(0xFF60A5FA) else Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Volume",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.BatteryFull,
                        contentDescription = "Battery",
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Clock / Calendar Flyout Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isCalendarOpen) Color(0x443B82F6) else Color.Transparent)
                        .clickable { onToggleCalendar() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("btn_taskbar_clock"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentTime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

data class TaskbarAppEntry(
    val appId: String,
    val name: String,
    val iconName: String,
    val isOpen: Boolean,
    val isActive: Boolean,
    val windowId: String?
)

@Composable
fun StartButton(
    isOpen: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isOpen) {
                    Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF60A5FA)))
                } else {
                    Brush.linearGradient(listOf(Color(0x333B82F6), Color(0x221E293B)))
                }
            )
            .border(
                1.dp,
                if (isOpen) Color(0xFF93C5FD) else Color(0x4460A5FA),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .testTag("btn_start_menu"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Window,
            contentDescription = "Start Menu",
            tint = if (isOpen) Color.White else Color(0xFF60A5FA),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TaskbarSearchPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(Color(0x22FFFFFF))
            .border(0.8.dp, Color(0x3394A3B8), RoundedCornerShape(17.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Search & AI",
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Copilot",
            tint = Color(0xFF818CF8),
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
fun TaskbarAppIcon(
    item: TaskbarAppEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = 38.dp, height = 44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (item.isActive) Color(0x443B82F6)
                else if (item.isOpen) Color(0x22FFFFFF)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIconView(
            iconName = item.iconName,
            size = 28.dp
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Open indicator pill / dot
        if (item.isOpen) {
            Box(
                modifier = Modifier
                    .size(width = if (item.isActive) 14.dp else 4.dp, height = 3.dp)
                    .background(
                        if (item.isActive) Color(0xFF60A5FA) else Color(0xFF94A3B8),
                        RoundedCornerShape(2.dp)
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

private fun getFormattedTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}
