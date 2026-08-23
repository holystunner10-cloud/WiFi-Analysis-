package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppIconView(
    iconName: String,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    val (icon, bgBrush, tintColor) = getAppIconMetadata(iconName)

    Box(
        modifier = modifier
            .size(size)
            .background(bgBrush, RoundedCornerShape((size.value * 0.26f).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconName,
            tint = tintColor,
            modifier = Modifier.size((size.value * 0.58f).dp)
        )
    }
}

fun getAppIconMetadata(iconName: String): Triple<ImageVector, Brush, Color> {
    return when (iconName) {
        "copilot" -> Triple(
            Icons.Default.AutoAwesome,
            Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF9333EA), Color(0xFFEC4899))),
            Color.White
        )
        "folder" -> Triple(
            Icons.Default.Folder,
            Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706))),
            Color.White
        )
        "store" -> Triple(
            Icons.Default.Store,
            Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
            Color.White
        )
        "code", "vscode" -> Triple(
            Icons.Default.Code,
            Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF2563EB))),
            Color.White
        )
        "terminal" -> Triple(
            Icons.Default.Terminal,
            Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B))),
            Color(0xFF4ADE80)
        )
        "browser" -> Triple(
            Icons.Default.Language,
            Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF06B6D4))),
            Color.White
        )
        "notepad" -> Triple(
            Icons.Default.Description,
            Brush.linearGradient(listOf(Color(0xFF475569), Color(0xFF334155))),
            Color.White
        )
        "taskmgr" -> Triple(
            Icons.Default.Memory,
            Brush.linearGradient(listOf(Color(0xFF16A34A), Color(0xFF059669))),
            Color.White
        )
        "paint", "photopea" -> Triple(
            Icons.Default.Palette,
            Brush.linearGradient(listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))),
            Color.White
        )
        "games", "chess" -> Triple(
            Icons.Default.SportsEsports,
            Brush.linearGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C))),
            Color.White
        )
        "calc", "graph" -> Triple(
            Icons.Default.Calculate,
            Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF0D9488))),
            Color.White
        )
        "settings" -> Triple(
            Icons.Default.Settings,
            Brush.linearGradient(listOf(Color(0xFF4B5563), Color(0xFF374151))),
            Color.White
        )
        "excalidraw" -> Triple(
            Icons.Default.ColorLens,
            Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5))),
            Color.White
        )
        "devdocs", "wikipedia" -> Triple(
            Icons.Default.MenuBook,
            Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF0F766E))),
            Color.White
        )
        "music" -> Triple(
            Icons.Default.Headphones,
            Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF059669))),
            Color.White
        )
        "android" -> Triple(
            Icons.Default.Android,
            Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF16A34A))),
            Color.White
        )
        else -> Triple(
            Icons.Default.Widgets,
            Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))),
            Color.White
        )
    }
}
