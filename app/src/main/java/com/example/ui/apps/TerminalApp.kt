package com.example.ui.apps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VirtualFile
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class TerminalLine(
    val text: String,
    val color: Color = Color(0xFF4ADE80),
    val isCommand: Boolean = false
)

@Composable
fun TerminalApp(
    initialPath: String = "C:\\Users\\Admin",
    listFiles: (String) -> List<VirtualFile>,
    readFile: (String) -> VirtualFile?,
    onCreateFile: (String, String, Boolean, String) -> Boolean,
    onDeleteFile: (String) -> Boolean,
    onAskGemini: (String) -> Unit,
    onInstallApp: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPath by remember { mutableStateOf(initialPath) }
    var inputCommand by remember { mutableStateOf("") }
    val history = remember {
        mutableListOf<TerminalLine>().apply {
            add(TerminalLine("Holy Stunner PC [Version 2.5.0 - Landscape Edition]", Color(0xFF60A5FA)))
            add(TerminalLine("(c) 2026 Holy Stunner PC Corporation. All rights reserved.", Color(0xFF94A3B8)))
            add(TerminalLine("Type 'help' to see all available CLI commands, 'ai <query>' for Holy Stunner AI, or 'neofetch'.", Color(0xFFE2E8F0)))
            add(TerminalLine(""))
        }
    }

    var lines by remember { mutableStateOf(history.toList()) }
    val listState = rememberLazyListState()

    var isMatrixRunning by remember { mutableStateOf(false) }

    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) {
            listState.animateScrollToItem(lines.size - 1)
        }
    }

    // Matrix Rain Effect animation if triggered
    LaunchedEffect(isMatrixRunning) {
        if (isMatrixRunning) {
            val chars = "010101XYZQWERT#$%&@*/<>+~"
            for (i in 1..25) {
                delay(120)
                val line = (1..32).map { chars.random() }.joinToString("")
                lines = lines + TerminalLine(line, Color(0xFF22C55E))
            }
            lines = lines + TerminalLine("[*] Matrix simulation cycle complete.", Color(0xFF60A5FA))
            isMatrixRunning = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF030712))
    ) {
        // Terminal Window Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x334ADE80), RoundedCornerShape(0.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "CLI",
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PowerShell / Bash Terminal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ADE80),
                    fontFamily = FontFamily.Monospace
                )
            }

            IconButton(
                onClick = {
                    lines = listOf(
                        TerminalLine("PC Desktop Terminal cleared.", Color(0xFF60A5FA))
                    )
                },
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CleaningServices,
                    contentDescription = "Clear",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Terminal Log Area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(lines) { line ->
                Text(
                    text = line.text,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = line.color,
                    lineHeight = 15.sp
                )
            }
        }

        // Input Command Prompt Line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0B132B))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "admin@android-pc:$currentPath$ ",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF60A5FA)
            )

            BasicTextField(
                value = inputCommand,
                onValueChange = { inputCommand = it },
                textStyle = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF4ADE80)
                ),
                cursorBrush = SolidColor(Color(0xFF4ADE80)),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (inputCommand.isNotBlank()) {
                            val cmd = inputCommand.trim()
                            lines = lines + TerminalLine("admin@android-pc:$currentPath$ $cmd", Color(0xFF93C5FD), isCommand = true)
                            val (newPath, outputLines) = handleCommand(
                                command = cmd,
                                currentPath = currentPath,
                                listFiles = listFiles,
                                readFile = readFile,
                                onCreateFile = onCreateFile,
                                onDeleteFile = onDeleteFile,
                                onAskGemini = onAskGemini,
                                onInstallApp = onInstallApp,
                                onTriggerMatrix = { isMatrixRunning = true }
                            )
                            currentPath = newPath
                            lines = lines + outputLines
                            inputCommand = ""
                        }
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("terminal_input_field")
            )
        }
    }
}

private fun handleCommand(
    command: String,
    currentPath: String,
    listFiles: (String) -> List<VirtualFile>,
    readFile: (String) -> VirtualFile?,
    onCreateFile: (String, String, Boolean, String) -> Boolean,
    onDeleteFile: (String) -> Boolean,
    onAskGemini: (String) -> Unit,
    onInstallApp: (String) -> Unit,
    onTriggerMatrix: () -> Unit
): Pair<String, List<TerminalLine>> {
    val parts = command.split(" ").filter { it.isNotBlank() }
    if (parts.isEmpty()) return Pair(currentPath, emptyList())

    val base = parts[0].lowercase()
    val arg1 = parts.getOrNull(1)
    val argRest = if (parts.size > 1) parts.drop(1).joinToString(" ") else ""

    var newPath = currentPath
    val results = mutableListOf<TerminalLine>()

    when (base) {
        "help" -> {
            results.add(TerminalLine("=== PC DESKTOP CLI COMMANDS ===", Color(0xFFF59E0B)))
            results.add(TerminalLine("• help               - Display this help manual"))
            results.add(TerminalLine("• ls / dir           - List files in current directory"))
            results.add(TerminalLine("• cd <path>          - Change directory (e.g. cd Projects, cd ..)"))
            results.add(TerminalLine("• cat <file>         - Output contents of a file"))
            results.add(TerminalLine("• echo <text>        - Echo text to console"))
            results.add(TerminalLine("• touch <name>       - Create an empty file"))
            results.add(TerminalLine("• mkdir <name>       - Create a new directory"))
            results.add(TerminalLine("• rm <path>          - Delete a file or directory"))
            results.add(TerminalLine("• neofetch           - Display system architecture & specs"))
            results.add(TerminalLine("• matrix             - Launch falling green Matrix rain"))
            results.add(TerminalLine("• gemini <prompt>    - Ask Gemini Copilot AI"))
            results.add(TerminalLine("• install <app_id>   - Install app from PC Store (e.g. vscode_web, photopea)"))
            results.add(TerminalLine("• top                - Monitor active system processes"))
            results.add(TerminalLine("• ping <host>        - Test network latency to host"))
            results.add(TerminalLine("• calc <math>        - Quick calculation (e.g. calc 12*45)"))
            results.add(TerminalLine("• date / whoami      - System date and user identity"))
            results.add(TerminalLine("• clear              - Reset screen"))
        }

        "clear", "cls" -> {
            results.add(TerminalLine("Screen cleared.", Color(0xFF60A5FA)))
        }

        "whoami" -> {
            results.add(TerminalLine("Administrator (Holy Stunner PC OS • UID: 1000)", Color(0xFF4ADE80)))
        }

        "date" -> {
            results.add(TerminalLine(SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(Date()), Color(0xFFE2E8F0)))
        }

        "ls", "dir" -> {
            val files = listFiles(currentPath)
            results.add(TerminalLine("Directory: $currentPath", Color(0xFF60A5FA)))
            results.add(TerminalLine(String.format("%-10s %-10s %s", "Mode", "Size", "Name"), Color(0xFF94A3B8)))
            results.add(TerminalLine("----------------------------------------", Color(0xFF334155)))
            files.forEach { file ->
                val mode = if (file.isDirectory) "d----" else "-a---"
                val size = if (file.isDirectory) "<DIR>" else "${file.sizeBytes} B"
                val col = if (file.isDirectory) Color(0xFFF59E0B) else Color(0xFFE2E8F0)
                results.add(TerminalLine(String.format("%-10s %-10s %s", mode, size, file.name), col))
            }
        }

        "cd" -> {
            if (arg1 == null || arg1 == "~" || arg1 == "") {
                newPath = "C:\\Users\\Admin"
            } else if (arg1 == "..") {
                if (currentPath.contains("\\")) {
                    val p = currentPath.substringBeforeLast('\\', "C:")
                    newPath = if (p.isBlank()) "C:" else p
                }
            } else {
                val target = if (arg1.startsWith("C:")) arg1 else "$currentPath\\$arg1"
                val files = listFiles(currentPath)
                val exists = files.any { it.name.equals(arg1, ignoreCase = true) && it.isDirectory }
                if (exists || target == "C:" || target == "C:\\Users" || target == "C:\\Users\\Admin") {
                    newPath = target
                } else {
                    results.add(TerminalLine("cd: error: directory '$arg1' does not exist", Color(0xFFEF4444)))
                }
            }
        }

        "cat" -> {
            if (arg1 == null) {
                results.add(TerminalLine("Usage: cat <filename>", Color(0xFFEF4444)))
            } else {
                val fullPath = if (arg1.startsWith("C:")) arg1 else "$currentPath\\$arg1"
                val file = readFile(fullPath)
                if (file != null && !file.isDirectory) {
                    file.content.lines().forEach {
                        results.add(TerminalLine(it, Color(0xFFE2E8F0)))
                    }
                } else {
                    results.add(TerminalLine("cat: $arg1: No such file", Color(0xFFEF4444)))
                }
            }
        }

        "echo" -> {
            results.add(TerminalLine(argRest, Color(0xFFE2E8F0)))
        }

        "touch" -> {
            if (arg1 == null) {
                results.add(TerminalLine("Usage: touch <filename>", Color(0xFFEF4444)))
            } else {
                val success = onCreateFile(currentPath, arg1, false, "")
                if (success) {
                    results.add(TerminalLine("Created file: $arg1", Color(0xFF4ADE80)))
                } else {
                    results.add(TerminalLine("touch: failed to create $arg1 (already exists)", Color(0xFFEF4444)))
                }
            }
        }

        "mkdir" -> {
            if (arg1 == null) {
                results.add(TerminalLine("Usage: mkdir <folder_name>", Color(0xFFEF4444)))
            } else {
                val success = onCreateFile(currentPath, arg1, true, "")
                if (success) {
                    results.add(TerminalLine("Created folder: $arg1", Color(0xFF4ADE80)))
                } else {
                    results.add(TerminalLine("mkdir: failed to create $arg1", Color(0xFFEF4444)))
                }
            }
        }

        "rm" -> {
            if (arg1 == null) {
                results.add(TerminalLine("Usage: rm <filename>", Color(0xFFEF4444)))
            } else {
                val fullPath = if (arg1.startsWith("C:")) arg1 else "$currentPath\\$arg1"
                val success = onDeleteFile(fullPath)
                if (success) {
                    results.add(TerminalLine("Deleted: $arg1", Color(0xFF4ADE80)))
                } else {
                    results.add(TerminalLine("rm: cannot remove '$arg1'", Color(0xFFEF4444)))
                }
            }
        }

        "neofetch" -> {
            val logo = """
       /\_/\      OS: Holy Stunner PC Edition v2.5
      ( o.o )     Kernel: Linux 6.1-x86_64
       > ^ <      Shell: Holy Stunner CLI Engine
      /     \     AI: Holy Stunner AI (Gemini Flash)
     (       )    Window Manager: Jetpack Compose Compositor
     (___)___)    RAM: 640MB / 4096MB (Dynamic)
                  Disk C:\: 128MB Virtual NTFS
"""
            logo.lines().forEach {
                results.add(TerminalLine(it, Color(0xFF60A5FA)))
            }
        }

        "matrix" -> {
            results.add(TerminalLine("[*] Entering Matrix reality stream...", Color(0xFF22C55E)))
            onTriggerMatrix()
        }

        "gemini", "ai", "stunner", "holy" -> {
            if (argRest.isBlank()) {
                results.add(TerminalLine("Usage: ai <your prompt or question>", Color(0xFFEC4899)))
            } else {
                results.add(TerminalLine("[*] Querying Holy Stunner AI: \"$argRest\"...", Color(0xFFEC4899)))
                results.add(TerminalLine("[✓] Response sent to Holy Stunner AI Studio window.", Color(0xFF93C5FD)))
                onAskGemini(argRest)
            }
        }

        "install" -> {
            if (arg1 == null) {
                results.add(TerminalLine("Usage: install <app_id> (e.g. vscode_web, photopea, spotify_web)", Color(0xFFEF4444)))
            } else {
                onInstallApp(arg1)
                results.add(TerminalLine("[+] Package '$arg1' installed and pinned to Desktop!", Color(0xFF4ADE80)))
            }
        }

        "top" -> {
            results.add(TerminalLine("PID   USER    CPU%   MEM%   COMMAND", Color(0xFF94A3B8)))
            results.add(TerminalLine("101   admin   4.2    18.0   desktop_compositor", Color(0xFF4ADE80)))
            results.add(TerminalLine("102   admin   1.5    9.5    gemini_daemon", Color(0xFF4ADE80)))
            results.add(TerminalLine("103   admin   0.8    4.2    vfs_daemon", Color(0xFF4ADE80)))
            results.add(TerminalLine("201   admin   2.1    12.4   terminal_host", Color(0xFF4ADE80)))
        }

        "ping" -> {
            val host = arg1 ?: "google.com"
            results.add(TerminalLine("PING $host (142.250.180.206): 56 data bytes", Color(0xFFE2E8F0)))
            results.add(TerminalLine("64 bytes from 142.250.180.206: icmp_seq=0 ttl=117 time=14.2 ms", Color(0xFF4ADE80)))
            results.add(TerminalLine("64 bytes from 142.250.180.206: icmp_seq=1 ttl=117 time=13.8 ms", Color(0xFF4ADE80)))
            results.add(TerminalLine("--- $host ping statistics ---", Color(0xFF94A3B8)))
            results.add(TerminalLine("2 packets transmitted, 2 packets received, 0.0% packet loss", Color(0xFF60A5FA)))
        }

        "calc" -> {
            if (argRest.isBlank()) {
                results.add(TerminalLine("Usage: calc <expression> (e.g. calc 25 * 4)", Color(0xFFEF4444)))
            } else {
                val ans = try {
                    val sanitized = argRest.replace(" ", "")
                    if (sanitized.contains("+")) {
                        val p = sanitized.split("+")
                        p[0].toDouble() + p[1].toDouble()
                    } else if (sanitized.contains("*")) {
                        val p = sanitized.split("*")
                        p[0].toDouble() * p[1].toDouble()
                    } else if (sanitized.contains("-")) {
                        val p = sanitized.split("-")
                        p[0].toDouble() - p[1].toDouble()
                    } else if (sanitized.contains("/")) {
                        val p = sanitized.split("/")
                        p[0].toDouble() / p[1].toDouble()
                    } else sanitized.toDouble()
                } catch (e: Exception) {
                    "Error parsing math"
                }
                results.add(TerminalLine("Result: $ans", Color(0xFFF59E0B)))
            }
        }

        else -> {
            results.add(TerminalLine("'$base' is not recognized as an internal or external command. Type 'help' for assistance.", Color(0xFFEF4444)))
        }
    }

    return Pair(newPath, results)
}
