package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DesktopSettings
import com.example.data.model.WallpaperType

@Composable
fun SettingsApp(
    settings: DesktopSettings,
    onUpdateSettings: (DesktopSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Settings Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color(0xFF60A5FA),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Holy Stunner PC Settings",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Customize wallpapers, Holy Stunner AI & launcher preferences",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Wallpaper Section
            item {
                Text(
                    text = "Desktop Wallpaper",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF93C5FD)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val wallpapers = listOf(
                    Triple(WallpaperType.CYBER_AI, "Cyberpunk AI Studio", Color(0xFF3B82F6)),
                    Triple(WallpaperType.DEEP_SPACE, "Deep Nebula Space", Color(0xFF6366F1)),
                    Triple(WallpaperType.NEON_GRID, "Synthwave Neon Grid", Color(0xFFEC4899)),
                    Triple(WallpaperType.MINIMAL_DARK, "Minimal Obsidian Dark", Color(0xFF334155)),
                    Triple(WallpaperType.NATURE_HORIZON, "Emerald Aurora Horizon", Color(0xFF059669))
                )

                wallpapers.forEach { (type, label, accent) ->
                    val isSelected = settings.wallpaperType == type
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0x333B82F6) else Color(0xFF0F172A))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF60A5FA) else Color(0x2260A5FA),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onUpdateSettings(settings.copy(wallpaperType = type))
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(accent, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }

                        if (isSelected) {
                            Text(
                                text = "Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF60A5FA)
                            )
                        }
                    }
                }
            }

            // AI Section
            item {
                HorizontalDivider(color = Color(0x2260A5FA))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Holy Stunner AI Engine",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC4899)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0x33EC4899), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Holy Stunner AI",
                        tint = Color(0xFFEC4899),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Holy Stunner AI Intelligence Subsystem",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Integrated Gemini Flash neural copilot for coding, reasoning, and PC automation.",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            // Launcher Behavior Section
            item {
                HorizontalDivider(color = Color(0x2260A5FA))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Android Home Launcher",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ADE80)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0x334ADE80), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color(0xFF4ADE80),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Default Home Screen",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Set Holy Stunner PC as your Android device's primary launcher.",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            // System About
            item {
                HorizontalDivider(color = Color(0x2260A5FA))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Holy Stunner PC OS v2.5.0 for Android • Landscape Multi-Window Engine",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}
