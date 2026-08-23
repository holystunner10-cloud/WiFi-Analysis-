package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.model.AppCategory
import com.example.data.model.AppType
import com.example.data.model.ChatMessage
import com.example.data.model.DesktopSettings
import com.example.data.model.MessageSender
import com.example.data.model.PcApp
import com.example.data.model.ProcessInfo
import com.example.data.model.VirtualFile
import com.example.data.model.WallpaperType
import com.example.data.model.WindowState
import com.example.data.repository.PcAppRepository
import com.example.data.repository.VirtualFileSystemRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class DesktopViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = PcAppRepository(application)
    private val vfsRepository = VirtualFileSystemRepository()
    private val geminiService = GeminiAiService()

    // Apps & System State
    val apps: StateFlow<List<PcApp>> = appRepository.apps
    val installedAndroidApps: StateFlow<List<PcApp>> = appRepository.installedAndroidApps
    val virtualFiles: StateFlow<List<VirtualFile>> = vfsRepository.files

    // Window Management
    private val _openWindows = MutableStateFlow<List<WindowState>>(emptyList())
    val openWindows: StateFlow<List<WindowState>> = _openWindows.asStateFlow()

    private val _activeWindowId = MutableStateFlow<String?>(null)
    val activeWindowId: StateFlow<String?> = _activeWindowId.asStateFlow()

    private var maxZIndex = 1f

    // Menus & Shell UI
    private val _isStartMenuOpen = MutableStateFlow(false)
    val isStartMenuOpen: StateFlow<Boolean> = _isStartMenuOpen.asStateFlow()

    private val _isSystemTrayOpen = MutableStateFlow(false)
    val isSystemTrayOpen: StateFlow<Boolean> = _isSystemTrayOpen.asStateFlow()

    private val _isCalendarOpen = MutableStateFlow(false)
    val isCalendarOpen: StateFlow<Boolean> = _isCalendarOpen.asStateFlow()

    private val _isContextMenuOpen = MutableStateFlow(false)
    val isContextMenuOpen: StateFlow<Boolean> = _isContextMenuOpen.asStateFlow()

    private val _startMenuSearchQuery = MutableStateFlow("")
    val startMenuSearchQuery: StateFlow<String> = _startMenuSearchQuery.asStateFlow()

    private val _settings = MutableStateFlow(DesktopSettings())
    val settings: StateFlow<DesktopSettings> = _settings.asStateFlow()

    // Copilot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.COPILOT,
                text = "👋 Welcome to **Holy Stunner PC**! I'm **Holy Stunner AI**, built directly into your desktop. How can I assist you today? You can ask me to write code, explore files, summarize documents, or run terminal commands."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Task Manager Processes & Live Metrics
    private val _processes = MutableStateFlow<List<ProcessInfo>>(emptyList())
    val processes: StateFlow<List<ProcessInfo>> = _processes.asStateFlow()

    private val _cpuUsage = MutableStateFlow(18f)
    val cpuUsage: StateFlow<Float> = _cpuUsage.asStateFlow()

    private val _ramUsageMb = MutableStateFlow(640)
    val ramUsageMb: StateFlow<Int> = _ramUsageMb.asStateFlow()

    // Desktop Selected Icon
    private val _selectedDesktopAppId = MutableStateFlow<String?>(null)
    val selectedDesktopAppId: StateFlow<String?> = _selectedDesktopAppId.asStateFlow()

    init {
        // Initial setup - open Holy Stunner AI on first launch
        launchApp("copilot")
        startHardwareMonitorSimulation()
    }

    // ==========================================
    // WINDOW OPERATIONS
    // ==========================================

    fun launchApp(appId: String, extraData: String? = null) {
        _isStartMenuOpen.value = false
        _isSystemTrayOpen.value = false
        _isCalendarOpen.value = false
        _isContextMenuOpen.value = false

        // Normalize Holy Stunner AI ID aliases
        val targetAppId = if (appId == "gemini_copilot" || appId == "holy_stunner_ai") "copilot" else appId

        // Check if Android app
        if (targetAppId.startsWith("android_")) {
            val pkg = targetAppId.removePrefix("android_")
            appRepository.launchAndroidApp(pkg)
            return
        }

        val app = apps.value.find { it.id == targetAppId || (targetAppId == "copilot" && it.id == "gemini_copilot") } ?: return

        val existing = _openWindows.value.find { it.appId == targetAppId || it.appId == app.id }
        if (existing != null) {
            // Restore and focus existing window
            maxZIndex += 1f
            _openWindows.value = _openWindows.value.map {
                if (it.id == existing.id) {
                    it.copy(isMinimized = false, zIndex = maxZIndex, extraData = extraData ?: it.extraData)
                } else it
            }
            _activeWindowId.value = existing.id
            return
        }

        // Open new window
        maxZIndex += 1f
        val windowCount = _openWindows.value.size
        val staggeredOffset = 18f + (windowCount % 5) * 20f

        val newWindow = WindowState(
            id = "win_${targetAppId}_${System.currentTimeMillis()}",
            appId = targetAppId,
            title = app.name,
            iconName = app.iconName,
            offsetX = staggeredOffset,
            offsetY = staggeredOffset + 6f,
            widthDp = when (targetAppId) {
                "arcade_hub", "calculator" -> 320f
                "code_studio", "web_browser", "pc_store" -> 440f
                "copilot" -> 380f
                else -> 380f
            },
            heightDp = when (targetAppId) {
                "calculator" -> 440f
                "code_studio", "web_browser", "pc_store" -> 420f
                "copilot" -> 450f
                else -> 390f
            },
            zIndex = maxZIndex,
            extraData = extraData
        )

        _openWindows.value = _openWindows.value + newWindow
        _activeWindowId.value = newWindow.id
        updateProcessList()
    }

    fun closeWindow(windowId: String) {
        _openWindows.value = _openWindows.value.filterNot { it.id == windowId }
        if (_activeWindowId.value == windowId) {
            val nextActive = _openWindows.value.filterNot { it.isMinimized }.maxByOrNull { it.zIndex }
            _activeWindowId.value = nextActive?.id
        }
        updateProcessList()
    }

    fun minimizeWindow(windowId: String) {
        _openWindows.value = _openWindows.value.map {
            if (it.id == windowId) it.copy(isMinimized = true) else it
        }
        if (_activeWindowId.value == windowId) {
            val nextActive = _openWindows.value.filterNot { it.isMinimized || it.id == windowId }.maxByOrNull { it.zIndex }
            _activeWindowId.value = nextActive?.id
        }
    }

    fun toggleMaximizeWindow(windowId: String) {
        _openWindows.value = _openWindows.value.map {
            if (it.id == windowId) it.copy(isMaximized = !it.isMaximized) else it
        }
        bringWindowToFront(windowId)
    }

    fun bringWindowToFront(windowId: String) {
        maxZIndex += 1f
        _openWindows.value = _openWindows.value.map {
            if (it.id == windowId) it.copy(zIndex = maxZIndex, isMinimized = false) else it
        }
        _activeWindowId.value = windowId
    }

    fun moveWindow(windowId: String, deltaX: Float, deltaY: Float) {
        _openWindows.value = _openWindows.value.map {
            if (it.id == windowId && !it.isMaximized) {
                it.copy(
                    offsetX = (it.offsetX + deltaX).coerceAtLeast(0f),
                    offsetY = (it.offsetY + deltaY).coerceAtLeast(0f)
                )
            } else it
        }
    }

    fun resizeWindow(windowId: String, deltaWidth: Float, deltaHeight: Float) {
        _openWindows.value = _openWindows.value.map {
            if (it.id == windowId && !it.isMaximized) {
                it.copy(
                    widthDp = (it.widthDp + deltaWidth).coerceIn(240f, 650f),
                    heightDp = (it.heightDp + deltaHeight).coerceIn(200f, 750f)
                )
            } else it
        }
    }

    // ==========================================
    // SHELL & MENU ACTIONS
    // ==========================================

    fun toggleStartMenu() {
        _isStartMenuOpen.value = !_isStartMenuOpen.value
        if (_isStartMenuOpen.value) {
            _isSystemTrayOpen.value = false
            _isCalendarOpen.value = false
            _isContextMenuOpen.value = false
        }
    }

    fun toggleSystemTray() {
        _isSystemTrayOpen.value = !_isSystemTrayOpen.value
        if (_isSystemTrayOpen.value) {
            _isStartMenuOpen.value = false
            _isCalendarOpen.value = false
            _isContextMenuOpen.value = false
        }
    }

    fun toggleCalendar() {
        _isCalendarOpen.value = !_isCalendarOpen.value
        if (_isCalendarOpen.value) {
            _isStartMenuOpen.value = false
            _isSystemTrayOpen.value = false
            _isContextMenuOpen.value = false
        }
    }

    fun toggleContextMenu() {
        _isContextMenuOpen.value = !_isContextMenuOpen.value
        if (_isContextMenuOpen.value) {
            _isStartMenuOpen.value = false
            _isSystemTrayOpen.value = false
            _isCalendarOpen.value = false
        }
    }

    fun closeAllOverlays() {
        _isStartMenuOpen.value = false
        _isSystemTrayOpen.value = false
        _isCalendarOpen.value = false
        _isContextMenuOpen.value = false
        _selectedDesktopAppId.value = null
    }

    fun updateStartMenuSearch(query: String) {
        _startMenuSearchQuery.value = query
    }

    fun setSelectedDesktopApp(appId: String?) {
        _selectedDesktopAppId.value = appId
    }

    // ==========================================
    // COPILOT AI ASSISTANT
    // ==========================================

    fun sendCopilotMessage(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage(sender = MessageSender.USER, text = prompt)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val response = geminiService.generateAssistantResponse(prompt)
            _isAiThinking.value = false
            val botMsg = ChatMessage(sender = MessageSender.COPILOT, text = response)
            _chatMessages.value = _chatMessages.value + botMsg
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = MessageSender.COPILOT,
                text = "✨ History cleared! Holy Stunner AI is ready for your next task."
            )
        )
    }

    // ==========================================
    // PC APP STORE & MANAGEMENT
    // ==========================================

    fun installApp(appId: String) {
        appRepository.installApp(appId)
    }

    fun uninstallApp(appId: String) {
        appRepository.uninstallApp(appId)
        val openWin = _openWindows.value.find { it.appId == appId }
        if (openWin != null) {
            closeWindow(openWin.id)
        }
    }

    fun togglePinDesktop(appId: String) {
        appRepository.togglePinDesktop(appId)
    }

    fun togglePinTaskbar(appId: String) {
        appRepository.togglePinTaskbar(appId)
    }

    fun addCustomWebApp(name: String, url: String, category: AppCategory) {
        appRepository.addCustomWebApp(name, url, category)
    }

    // ==========================================
    // VIRTUAL FILE SYSTEM
    // ==========================================

    fun listFiles(path: String): List<VirtualFile> {
        return vfsRepository.listFiles(path)
    }

    fun readFile(path: String): VirtualFile? {
        return vfsRepository.getFile(path)
    }

    fun saveFileContent(path: String, content: String) {
        vfsRepository.saveFileContent(path, content)
    }

    fun createFile(parentPath: String, name: String, isDirectory: Boolean, content: String = ""): Boolean {
        return vfsRepository.createFile(parentPath, name, isDirectory, content)
    }

    fun deleteFile(path: String): Boolean {
        return vfsRepository.deleteFile(path)
    }

    // ==========================================
    // SETTINGS & SYSTEM CONTROLS
    // ==========================================

    fun updateSettings(newSettings: DesktopSettings) {
        _settings.value = newSettings
    }

    // ==========================================
    // TASK MANAGER & DIAGNOSTICS
    // ==========================================

    fun killProcess(pid: Int) {
        _processes.value = _processes.value.filterNot { it.pid == pid }
    }

    private fun updateProcessList() {
        val baseList = mutableListOf(
            ProcessInfo(101, "Desktop Shell (Compositor)", 4.2f, 180, "Running", isKillable = false),
            ProcessInfo(102, "Holy Stunner AI Engine (Background)", 1.5f, 95, "Idle", isKillable = false),
            ProcessInfo(103, "Virtual File System Daemon", 0.8f, 42, "Running", isKillable = false)
        )

        _openWindows.value.forEachIndexed { idx, win ->
            baseList.add(
                ProcessInfo(
                    pid = 200 + idx,
                    name = "${win.title} (${win.appId})",
                    cpuUsagePercent = (Random.nextFloat() * 6.5f + 1.2f).coerceAtLeast(0.5f),
                    memoryUsageMb = Random.nextInt(45, 120),
                    status = if (win.isMinimized) "Suspended" else "Active",
                    isKillable = true
                )
            )
        }

        _processes.value = baseList
    }

    private fun startHardwareMonitorSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(3000)
                val winCount = _openWindows.value.size
                val baseCpu = 8f + winCount * 3.5f
                _cpuUsage.value = (baseCpu + Random.nextFloat() * 6f).coerceIn(5f, 98f)
                _ramUsageMb.value = 520 + winCount * 65 + Random.nextInt(-15, 25)
                updateProcessList()
            }
        }
    }
}
