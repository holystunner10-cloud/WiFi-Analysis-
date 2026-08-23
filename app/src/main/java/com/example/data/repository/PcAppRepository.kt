package com.example.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.data.model.AppCategory
import com.example.data.model.AppType
import com.example.data.model.PcApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PcAppRepository(private val context: Context) {

    private val defaultApps = listOf(
        PcApp(
            id = "copilot",
            name = "Holy Stunner AI",
            category = AppCategory.SYSTEM,
            type = AppType.NATIVE,
            description = "Intelligent built-in AI desktop assistant. Code, write, calculate, and automate with Holy Stunner AI.",
            iconName = "copilot",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "file_explorer",
            name = "File Explorer",
            category = AppCategory.SYSTEM,
            type = AppType.NATIVE,
            description = "Manage virtual C:\\ drive files, project folders, documents, and disk storage.",
            iconName = "folder",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "pc_store",
            name = "PC App Store",
            category = AppCategory.SYSTEM,
            type = AppType.NATIVE,
            description = "Browse, install, and configure PC applications, web tools, and developer utilities.",
            iconName = "store",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "code_studio",
            name = "Code Studio IDE",
            category = AppCategory.DEVELOPMENT,
            type = AppType.NATIVE,
            description = "Lightweight code editor & runner for Python, JavaScript, HTML, and Markdown with AI support.",
            iconName = "code",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "terminal",
            name = "Terminal (CLI)",
            category = AppCategory.DEVELOPMENT,
            type = AppType.NATIVE,
            description = "PowerShell & Bash command console with system diagnostics, file manipulation, and AI tools.",
            iconName = "terminal",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "web_browser",
            name = "Edge Browser",
            category = AppCategory.PRODUCTIVITY,
            type = AppType.NATIVE,
            description = "Desktop-grade web browser with multi-tab support, developer bookmarks, and desktop mode.",
            iconName = "browser",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = true
        ),
        PcApp(
            id = "notepad",
            name = "Notepad Pro",
            category = AppCategory.PRODUCTIVITY,
            type = AppType.NATIVE,
            description = "Fast text and markdown editor with AI auto-complete and disk save support.",
            iconName = "notepad",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),
        PcApp(
            id = "task_manager",
            name = "Task Manager",
            category = AppCategory.SYSTEM,
            type = AppType.NATIVE,
            description = "Real-time process monitor, CPU/RAM performance charts, and process control.",
            iconName = "taskmgr",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),
        PcApp(
            id = "paint_studio",
            name = "Paint & Canvas",
            category = AppCategory.CREATIVE,
            type = AppType.NATIVE,
            description = "Digital sketchpad and drawing canvas with brushes, geometric shapes, and export tools.",
            iconName = "paint",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),
        PcApp(
            id = "arcade_hub",
            name = "Arcade & Games",
            category = AppCategory.GAMES,
            type = AppType.NATIVE,
            description = "Classic PC games including Minesweeper with difficulty modes and Retro Snake.",
            iconName = "games",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),
        PcApp(
            id = "calculator",
            name = "Calculator",
            category = AppCategory.UTILITIES,
            type = AppType.NATIVE,
            description = "Standard & Scientific desktop calculator with calculation history tape.",
            iconName = "calc",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),
        PcApp(
            id = "settings",
            name = "PC Settings",
            category = AppCategory.SYSTEM,
            type = AppType.NATIVE,
            description = "Personalize desktop wallpapers, themes, taskbar layout, and system options.",
            iconName = "settings",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false
        ),

        // Store Catalog Apps (Ready for 1-Click Install)
        PcApp(
            id = "vscode_web",
            name = "VS Code Web",
            category = AppCategory.DEVELOPMENT,
            type = AppType.WEB,
            description = "Full Visual Studio Code running directly in the browser with extensions and syntax highlighting.",
            iconName = "vscode",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://vscode.dev",
            author = "Microsoft",
            downloadSizeMb = 18.2f
        ),
        PcApp(
            id = "photopea",
            name = "Photopea Graphics",
            category = AppCategory.CREATIVE,
            type = AppType.WEB,
            description = "Advanced online Photoshop alternative supporting PSD, AI, Sketch, and RAW image editing.",
            iconName = "photopea",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://www.photopea.com",
            author = "Ivan Kutskir",
            downloadSizeMb = 8.5f
        ),
        PcApp(
            id = "excalidraw",
            name = "Excalidraw Board",
            category = AppCategory.CREATIVE,
            type = AppType.WEB,
            description = "Virtual collaborative whiteboard tool that lets you easily sketch diagrams with a hand-drawn feel.",
            iconName = "excalidraw",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://excalidraw.com",
            author = "Excalidraw",
            downloadSizeMb = 4.2f
        ),
        PcApp(
            id = "devdocs",
            name = "DevDocs API Hub",
            category = AppCategory.DEVELOPMENT,
            type = AppType.WEB,
            description = "Fast, offline-capable documentation browser for 100+ developer languages and frameworks.",
            iconName = "devdocs",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://devdocs.io",
            author = "FreeCodeCamp",
            downloadSizeMb = 6.1f
        ),
        PcApp(
            id = "wikipedia_app",
            name = "Wikipedia Desktop",
            category = AppCategory.PRODUCTIVITY,
            type = AppType.WEB,
            description = "The free encyclopedia with a clean, high-speed distraction-free reading layout.",
            iconName = "wikipedia",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://en.m.wikipedia.org",
            author = "Wikimedia",
            downloadSizeMb = 3.8f
        ),
        PcApp(
            id = "spotify_web",
            name = "Spotify Player",
            category = AppCategory.UTILITIES,
            type = AppType.WEB,
            description = "Stream millions of songs, podcasts, and curated playlists on your desktop.",
            iconName = "music",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://open.spotify.com",
            author = "Spotify",
            downloadSizeMb = 14.0f
        ),
        PcApp(
            id = "desmos_calc",
            name = "Desmos Math Lab",
            category = AppCategory.UTILITIES,
            type = AppType.WEB,
            description = "Explore math with beautiful, free graphing calculator software and scientific tools.",
            iconName = "graph",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://www.desmos.com/calculator",
            author = "Desmos",
            downloadSizeMb = 5.4f
        ),
        PcApp(
            id = "chess_app",
            name = "Chess Master",
            category = AppCategory.GAMES,
            type = AppType.WEB,
            description = "Play chess against smart bot engines, solve tactical puzzles, and analyze games.",
            iconName = "chess",
            isInstalled = false,
            isPinnedDesktop = false,
            webUrl = "https://lichess.org",
            author = "Lichess",
            downloadSizeMb = 7.9f
        )
    )

    private val _apps = MutableStateFlow<List<PcApp>>(defaultApps)
    val apps: StateFlow<List<PcApp>> = _apps.asStateFlow()

    private val _installedAndroidApps = MutableStateFlow<List<PcApp>>(emptyList())
    val installedAndroidApps: StateFlow<List<PcApp>> = _installedAndroidApps.asStateFlow()

    init {
        loadInstalledAndroidApps()
    }

    fun installApp(appId: String) {
        _apps.value = _apps.value.map {
            if (it.id == appId) {
                it.copy(isInstalled = true, isPinnedDesktop = true)
            } else it
        }
    }

    fun uninstallApp(appId: String) {
        _apps.value = _apps.value.map {
            if (it.id == appId) {
                it.copy(isInstalled = false, isPinnedDesktop = false, isPinnedTaskbar = false)
            } else it
        }
    }

    fun togglePinDesktop(appId: String) {
        _apps.value = _apps.value.map {
            if (it.id == appId) {
                it.copy(isPinnedDesktop = !it.isPinnedDesktop)
            } else it
        }
    }

    fun togglePinTaskbar(appId: String) {
        _apps.value = _apps.value.map {
            if (it.id == appId) {
                it.copy(isPinnedTaskbar = !it.isPinnedTaskbar)
            } else it
        }
    }

    fun addCustomWebApp(name: String, url: String, category: AppCategory) {
        val safeUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) "https://$url" else url
        val id = "custom_" + System.currentTimeMillis()
        val newApp = PcApp(
            id = id,
            name = name,
            category = category,
            type = AppType.CUSTOM,
            description = "Custom installed web app: $safeUrl",
            iconName = "web_custom",
            isInstalled = true,
            isPinnedDesktop = true,
            isPinnedTaskbar = false,
            webUrl = safeUrl,
            author = "User Created",
            downloadSizeMb = 1.0f
        )
        _apps.value = _apps.value + newApp
    }

    private fun loadInstalledAndroidApps() {
        try {
            val pm = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            val androidAppsList = resolveInfos.mapNotNull { resolveInfo ->
                val pkgName = resolveInfo.activityInfo.packageName
                if (pkgName == context.packageName) return@mapNotNull null
                val label = resolveInfo.loadLabel(pm).toString()
                PcApp(
                    id = "android_$pkgName",
                    name = label,
                    category = AppCategory.ANDROID,
                    type = AppType.ANDROID,
                    description = "Android System App ($pkgName)",
                    iconName = "android",
                    isInstalled = true,
                    isPinnedDesktop = false,
                    isPinnedTaskbar = false,
                    packageName = pkgName,
                    author = "Android Device"
                )
            }.sortedBy { it.name }
            _installedAndroidApps.value = androidAppsList
        } catch (e: Exception) {
            _installedAndroidApps.value = emptyList()
        }
    }

    fun launchAndroidApp(packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}
