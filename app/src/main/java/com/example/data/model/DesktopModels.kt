package com.example.data.model

enum class AppCategory {
    PRODUCTIVITY,
    DEVELOPMENT,
    SYSTEM,
    CREATIVE,
    UTILITIES,
    GAMES,
    WEB,
    ANDROID
}

enum class AppType {
    NATIVE,
    WEB,
    ANDROID,
    CUSTOM
}

data class PcApp(
    val id: String,
    val name: String,
    val category: AppCategory,
    val type: AppType = AppType.NATIVE,
    val description: String,
    val iconName: String = "app",
    val isInstalled: Boolean = true,
    val isPinnedDesktop: Boolean = true,
    val isPinnedTaskbar: Boolean = false,
    val webUrl: String? = null,
    val packageName: String? = null,
    val version: String = "1.0",
    val author: String = "System",
    val downloadSizeMb: Float = 12.5f
)

data class WindowState(
    val id: String,
    val appId: String,
    val title: String,
    val iconName: String = "app",
    val isMinimized: Boolean = false,
    val isMaximized: Boolean = false,
    val offsetX: Float = 20f,
    val offsetY: Float = 20f,
    val widthDp: Float = 360f,
    val heightDp: Float = 480f,
    val zIndex: Float = 1f,
    val extraData: String? = null
)

data class VirtualFile(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val content: String = "",
    val sizeBytes: Long = 0,
    val lastModified: Long = System.currentTimeMillis(),
    val extension: String = ""
)

enum class MessageSender {
    USER,
    COPILOT,
    SYSTEM
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val codeSnippet: String? = null,
    val actions: List<String> = emptyList()
)

data class ProcessInfo(
    val pid: Int,
    val name: String,
    val cpuUsagePercent: Float,
    val memoryUsageMb: Int,
    val status: String = "Running",
    val isKillable: Boolean = true
)

enum class WallpaperType(val label: String) {
    CYBER_AI("Cyber Neon Glass (AI)"),
    DEEP_SPACE("Deep Space Nebula"),
    NEON_GRID("Retro Synthwave Grid"),
    MINIMAL_DARK("Obsidian Midnight"),
    NATURE_HORIZON("Nordic Aurora")
}

data class DesktopSettings(
    val wallpaperType: WallpaperType = WallpaperType.CYBER_AI,
    val isDarkMode: Boolean = true,
    val showWidgets: Boolean = true,
    val taskbarCentered: Boolean = true,
    val volumeLevel: Float = 0.75f,
    val brightnessLevel: Float = 0.85f,
    val wifiEnabled: Boolean = true,
    val bluetoothEnabled: Boolean = true,
    val airplaneMode: Boolean = false
)
