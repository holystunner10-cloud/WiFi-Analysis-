package com.example.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.AppType
import com.example.data.model.VirtualFile
import com.example.ui.DesktopViewModel
import com.example.ui.apps.AiCopilotApp
import com.example.ui.apps.ArcadeHubApp
import com.example.ui.apps.CalculatorApp
import com.example.ui.apps.CodeStudioApp
import com.example.ui.apps.FileExplorerApp
import com.example.ui.apps.NotepadApp
import com.example.ui.apps.PaintStudioApp
import com.example.ui.apps.PcAppStoreApp
import com.example.ui.apps.SettingsApp
import com.example.ui.apps.TaskManagerApp
import com.example.ui.apps.TerminalApp
import com.example.ui.apps.WebBrowserApp
import com.example.ui.components.CalendarFlyout
import com.example.ui.components.DesktopContextMenu
import com.example.ui.components.DesktopIconsGrid
import com.example.ui.components.DesktopTaskbar
import com.example.ui.components.DesktopWallpaper
import com.example.ui.components.DesktopWidgets
import com.example.ui.components.StartMenu
import com.example.ui.components.SystemTrayFlyout
import com.example.ui.components.WindowFrame

@Composable
fun DesktopScreen(
    viewModel: DesktopViewModel,
    modifier: Modifier = Modifier
) {
    val apps by viewModel.apps.collectAsState()
    val installedAndroidApps by viewModel.installedAndroidApps.collectAsState()
    val openWindows by viewModel.openWindows.collectAsState()
    val activeWindowId by viewModel.activeWindowId.collectAsState()
    val selectedDesktopAppId by viewModel.selectedDesktopAppId.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val isStartMenuOpen by viewModel.isStartMenuOpen.collectAsState()
    val isSystemTrayOpen by viewModel.isSystemTrayOpen.collectAsState()
    val isCalendarOpen by viewModel.isCalendarOpen.collectAsState()
    val isContextMenuOpen by viewModel.isContextMenuOpen.collectAsState()
    val startMenuSearchQuery by viewModel.startMenuSearchQuery.collectAsState()

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    val cpuUsage by viewModel.cpuUsage.collectAsState()
    val ramUsageMb by viewModel.ramUsageMb.collectAsState()
    val processes by viewModel.processes.collectAsState()

    DesktopWallpaper(
        wallpaperType = settings.wallpaperType,
        modifier = modifier
            .fillMaxSize()
            .testTag("desktop_canvas")
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        viewModel.setSelectedDesktopApp(null)
                        viewModel.closeAllOverlays()
                    },
                    onLongPress = {
                        viewModel.closeAllOverlays()
                        viewModel.toggleContextMenu()
                    }
                )
            }
    ) {
        // Desktop Top-Right Widgets
        DesktopWidgets(
            cpuUsage = cpuUsage,
            ramUsageMb = ramUsageMb,
            activeWindowsCount = openWindows.count { !it.isMinimized },
            onOpenCopilot = {
                viewModel.launchApp("copilot")
            },
            onOpenTaskManager = {
                viewModel.launchApp("task_manager")
            }
        )

        // Desktop Shortcuts Grid
        DesktopIconsGrid(
            apps = apps,
            selectedAppId = selectedDesktopAppId,
            onSelectApp = { viewModel.setSelectedDesktopApp(it) },
            onOpenApp = { viewModel.launchApp(it) },
            onLongClickApp = { viewModel.toggleContextMenu() }
        )

        // Open Floating Application Windows
        openWindows.forEach { window ->
            val isActive = window.id == activeWindowId && !window.isMinimized

            WindowFrame(
                window = window,
                isActive = isActive,
                onFocus = { viewModel.bringWindowToFront(window.id) },
                onClose = { viewModel.closeWindow(window.id) },
                onMinimize = { viewModel.minimizeWindow(window.id) },
                onToggleMaximize = { viewModel.toggleMaximizeWindow(window.id) },
                onMove = { dx, dy -> viewModel.moveWindow(window.id, dx, dy) },
                onResize = { dw, dh -> viewModel.resizeWindow(window.id, dw, dh) }
            ) {
                val appObj = apps.find { it.id == window.appId }

                when (window.appId) {
                    "copilot" -> {
                        AiCopilotApp(
                            messages = chatMessages,
                            isThinking = isAiThinking,
                            onSendMessage = { viewModel.sendCopilotMessage(it) },
                            onClearChat = { viewModel.clearChat() },
                            onOpenCodeStudioWithCode = { code ->
                                viewModel.launchApp("code_studio")
                            }
                        )
                    }

                    "pc_store" -> {
                        PcAppStoreApp(
                            apps = apps,
                            installedAndroidApps = installedAndroidApps,
                            onInstallApp = { viewModel.installApp(it) },
                            onUninstallApp = { viewModel.uninstallApp(it) },
                            onOpenApp = { viewModel.launchApp(it) },
                            onTogglePinDesktop = { viewModel.togglePinDesktop(it) },
                            onAddCustomWebApp = { name, url, category ->
                                viewModel.addCustomWebApp(name, url, category)
                            }
                        )
                    }

                    "file_explorer" -> {
                        FileExplorerApp(
                            listFiles = { path -> viewModel.listFiles(path) },
                            onCreateFile = { path, name, isDir, content ->
                                viewModel.createFile(path, name, isDir, content)
                            },
                            onDeleteFile = { path -> viewModel.deleteFile(path) },
                            onOpenFileInNotepad = { file ->
                                viewModel.launchApp("notepad")
                            },
                            onOpenFileInCodeStudio = { file ->
                                viewModel.launchApp("code_studio")
                            },
                            onOpenTerminalWithPath = { path ->
                                viewModel.launchApp("terminal")
                            }
                        )
                    }

                    "code_studio" -> {
                        CodeStudioApp(
                            onSaveCodeToFile = { path, content ->
                                viewModel.saveFileContent(path, content)
                            },
                            onAskAiForCode = { prompt ->
                                viewModel.launchApp("copilot")
                                viewModel.sendCopilotMessage(prompt)
                            }
                        )
                    }

                    "terminal" -> {
                        TerminalApp(
                            listFiles = { path -> viewModel.listFiles(path) },
                            readFile = { path -> viewModel.readFile(path) },
                            onCreateFile = { path, name, isDir, content ->
                                viewModel.createFile(path, name, isDir, content)
                            },
                            onDeleteFile = { path -> viewModel.deleteFile(path) },
                            onAskGemini = { prompt ->
                                viewModel.launchApp("copilot")
                                viewModel.sendCopilotMessage(prompt)
                            },
                            onInstallApp = { appId ->
                                viewModel.installApp(appId)
                            }
                        )
                    }

                    "web_browser" -> {
                        val initialUrl = appObj?.webUrl ?: "https://www.wikipedia.org"
                        WebBrowserApp(initialUrl = initialUrl)
                    }

                    "notepad" -> {
                        NotepadApp(
                            onSaveFile = { path, content ->
                                viewModel.saveFileContent(path, content)
                            },
                            onAskAiSummarize = { prompt ->
                                viewModel.launchApp("copilot")
                                viewModel.sendCopilotMessage(prompt)
                            }
                        )
                    }

                    "task_manager" -> {
                        TaskManagerApp(
                            cpuUsage = cpuUsage,
                            ramUsageMb = ramUsageMb,
                            processes = processes,
                            onKillProcess = { pid -> viewModel.killProcess(pid) }
                        )
                    }

                    "paint" -> {
                        PaintStudioApp()
                    }

                    "arcade_hub" -> {
                        ArcadeHubApp()
                    }

                    "calculator" -> {
                        CalculatorApp()
                    }

                    "settings" -> {
                        SettingsApp(
                            settings = settings,
                            onUpdateSettings = { viewModel.updateSettings(it) }
                        )
                    }

                    else -> {
                        if (appObj?.type == AppType.WEB) {
                            WebBrowserApp(initialUrl = appObj.webUrl ?: "https://www.google.com")
                        } else {
                            NotepadApp(
                                onSaveFile = { path, content ->
                                    viewModel.saveFileContent(path, content)
                                },
                                onAskAiSummarize = { prompt ->
                                    viewModel.launchApp("copilot")
                                    viewModel.sendCopilotMessage(prompt)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Desktop Context Menu Overlay
        DesktopContextMenu(
            isOpen = isContextMenuOpen,
            onNewFolder = {
                viewModel.createFile("C:\\Users\\Admin\\Desktop", "New Folder", true, "")
            },
            onNewFile = {
                viewModel.createFile("C:\\Users\\Admin\\Desktop", "New Document.txt", false, "Sample notes")
            },
            onOpenTerminal = { viewModel.launchApp("terminal") },
            onOpenStore = { viewModel.launchApp("pc_store") },
            onOpenSettings = { viewModel.launchApp("settings") },
            onOpenCopilot = { viewModel.launchApp("copilot") },
            onClose = { viewModel.closeAllOverlays() }
        )

        // Start Menu Overlay
        StartMenu(
            isOpen = isStartMenuOpen,
            apps = apps,
            recentFiles = viewModel.listFiles("C:\\Users\\Admin\\Documents") + viewModel.listFiles("C:\\Users\\Admin\\Projects"),
            searchQuery = startMenuSearchQuery,
            onSearchChange = { viewModel.updateStartMenuSearch(it) },
            onOpenApp = { viewModel.launchApp(it) },
            onOpenFile = { file ->
                if (file.extension in listOf("py", "js", "html", "kt")) {
                    viewModel.launchApp("code_studio")
                } else {
                    viewModel.launchApp("notepad")
                }
            },
            onOpenCopilotWithQuery = { query ->
                viewModel.launchApp("copilot")
                viewModel.sendCopilotMessage(query)
            },
            onCloseMenu = { viewModel.closeAllOverlays() }
        )

        // System Tray Quick Settings Flyout
        SystemTrayFlyout(
            isOpen = isSystemTrayOpen,
            wifiEnabled = settings.wifiEnabled,
            bluetoothEnabled = settings.bluetoothEnabled,
            airplaneMode = settings.airplaneMode,
            volumeLevel = settings.volumeLevel,
            brightnessLevel = settings.brightnessLevel,
            onToggleWifi = { viewModel.updateSettings(settings.copy(wifiEnabled = !settings.wifiEnabled)) },
            onToggleBluetooth = { viewModel.updateSettings(settings.copy(bluetoothEnabled = !settings.bluetoothEnabled)) },
            onToggleAirplane = { viewModel.updateSettings(settings.copy(airplaneMode = !settings.airplaneMode)) },
            onVolumeChange = { viewModel.updateSettings(settings.copy(volumeLevel = it)) },
            onBrightnessChange = { viewModel.updateSettings(settings.copy(brightnessLevel = it)) },
            onOpenSettings = { viewModel.launchApp("settings") },
            onOpenTaskManager = { viewModel.launchApp("task_manager") },
            onClose = { viewModel.closeAllOverlays() }
        )

        // Calendar / Date Flyout
        CalendarFlyout(
            isOpen = isCalendarOpen,
            onClose = { viewModel.closeAllOverlays() }
        )

        // Bottom Desktop Taskbar
        DesktopTaskbar(
            isStartMenuOpen = isStartMenuOpen,
            isSystemTrayOpen = isSystemTrayOpen,
            isCalendarOpen = isCalendarOpen,
            pinnedApps = apps,
            openWindows = openWindows,
            activeWindowId = activeWindowId,
            wifiEnabled = settings.wifiEnabled,
            onToggleStartMenu = { viewModel.toggleStartMenu() },
            onToggleSystemTray = { viewModel.toggleSystemTray() },
            onToggleCalendar = { viewModel.toggleCalendar() },
            onOpenApp = { viewModel.launchApp(it) },
            onFocusOrMinimizeWindow = { winId ->
                if (winId == activeWindowId) {
                    val win = openWindows.find { it.id == winId }
                    if (win?.isMinimized == false) {
                        viewModel.minimizeWindow(winId)
                    } else {
                        viewModel.bringWindowToFront(winId)
                    }
                } else {
                    viewModel.bringWindowToFront(winId)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
