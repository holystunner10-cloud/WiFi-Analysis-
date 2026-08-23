package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VibrantBlue,
    onPrimary = Color.White,
    primaryContainer = VibrantBlueLight,
    onPrimaryContainer = VibrantBlueDark,
    secondary = VibrantGreen,
    onSecondary = Color.White,
    secondaryContainer = VibrantGreenLight,
    onSecondaryContainer = Color(0xFF14532D),
    tertiary = VibrantIndigo,
    onTertiary = Color.White,
    tertiaryContainer = VibrantIndigoLight,
    onTertiaryContainer = Color(0xFF312E81),
    background = LightBackground,
    onBackground = Slate900,
    surface = LightSurface,
    onSurface = Slate900,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Slate500,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle,
    error = VibrantRose,
    onError = Color.White,
    errorContainer = VibrantRoseLight,
    onErrorContainer = Color(0xFF991B1B)
)

private val DarkColorScheme = darkColorScheme(
    primary = VibrantBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = VibrantBlueLight,
    secondary = VibrantGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF14532D),
    onSecondaryContainer = VibrantGreenLight,
    tertiary = VibrantIndigo,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkBorder,
    error = VibrantRose
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to Modern Cyber Dark Theme for PC Desktop
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


