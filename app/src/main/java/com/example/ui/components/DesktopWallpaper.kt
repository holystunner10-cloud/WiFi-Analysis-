package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.WallpaperType

import androidx.compose.foundation.layout.BoxScope

@Composable
fun DesktopWallpaper(
    wallpaperType: WallpaperType,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (wallpaperType) {
            WallpaperType.CYBER_AI -> {
                Image(
                    painter = painterResource(id = R.drawable.cyber_desktop_wallpaper_1787493422620),
                    contentDescription = "Desktop Wallpaper",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Subtle dark glass gradient overlay for optimal icon readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x220B132B),
                                    Color(0x660F172A),
                                    Color(0xAA020617)
                                )
                            )
                        )
                )
            }
            WallpaperType.DEEP_SPACE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A),
                                    Color(0xFF020617)
                                )
                            )
                        )
                )
            }
            WallpaperType.NEON_GRID -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF311042),
                                    Color(0xFF111827),
                                    Color(0xFF0B192C)
                                )
                            )
                        )
                )
            }
            WallpaperType.MINIMAL_DARK -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0B0F17))
                )
            }
            WallpaperType.NATURE_HORIZON -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF064E3B),
                                    Color(0xFF062828),
                                    Color(0xFF021217)
                                )
                            )
                        )
                )
            }
        }

        content()
    }
}
