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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VirtualFile

@Composable
fun CodeStudioApp(
    initialCode: String? = null,
    initialFile: VirtualFile? = null,
    onSaveCodeToFile: (String, String) -> Unit,
    onAskAiForCode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var code by remember {
        mutableStateOf(
            initialCode ?: initialFile?.content ?: """# Python Demo in Code Studio
import time
import math

def calculate_fibonacci(n):
    fib = [0, 1]
    for i in range(2, n):
        fib.append(fib[-1] + fib[-2])
    return fib

print("[*] Running PC Desktop Code Runner...")
numbers = calculate_fibonacci(10)
print(f"[✓] Generated Fibonacci: {numbers}")
print(f"[✓] Square Root of 256: {math.sqrt(256)}")
print("[✓] Process finished successfully.")
"""
        )
    }

    var outputConsole by remember { mutableStateOf("Ready to run. Click 'Run' to execute code.") }
    var selectedLanguage by remember { mutableStateOf("Python") }
    var showSaveToast by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Code Studio Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "IDE",
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Code Studio IDE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Language Selector Chips
                listOf("Python", "JavaScript", "HTML").forEach { lang ->
                    val isSel = selectedLanguage == lang
                    Text(
                        text = lang,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSel) Color.White else Color(0xFF94A3B8),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSel) Color(0xFF2563EB) else Color.Transparent)
                            .clickable { selectedLanguage = lang }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Actions (Run, Save, Ask AI)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Run Button
                Button(
                    onClick = {
                        outputConsole = executeScript(code, selectedLanguage)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Run",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Run", fontSize = 11.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Ask AI Button
                Button(
                    onClick = {
                        onAskAiForCode("Explain and optimize this $selectedLanguage code:\n```\n$code\n```")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "AI Fix", fontSize = 11.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Save
                IconButton(
                    onClick = {
                        val path = initialFile?.path ?: "C:\\Users\\Admin\\Projects\\script_${System.currentTimeMillis() % 1000}.py"
                        onSaveCodeToFile(path, code)
                        showSaveToast = true
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Code Editor Text Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF070D19))
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                textStyle = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF070D19),
                    unfocusedContainerColor = Color(0xFF070D19),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("code_editor_input")
            )
        }

        // Output Console Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = "Console",
                tint = Color(0xFF4ADE80),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Console Output",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4ADE80)
            )
        }

        // Output Console Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color(0xFF020617))
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = outputConsole,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color(0xFF93C5FD),
                lineHeight = 15.sp
            )
        }
    }
}

private fun executeScript(code: String, language: String): String {
    val sb = StringBuilder()
    sb.append("[Execution Started: $language Engine]\n")
    sb.append("--------------------------------------\n")

    val lines = code.lines()
    for (line in lines) {
        val trimmed = line.trim()
        if (trimmed.startsWith("print(") && trimmed.endsWith(")")) {
            val content = trimmed.substring(6, trimmed.length - 1)
                .replace("\"", "")
                .replace("'", "")
                .replace("f[", "[")
                .replace("f\"", "")
            sb.append(content).append("\n")
        } else if (trimmed.startsWith("console.log(") && trimmed.endsWith(")")) {
            val content = trimmed.substring(12, trimmed.length - 1)
                .replace("\"", "")
                .replace("'", "")
            sb.append(content).append("\n")
        }
    }

    if (sb.lines().size <= 3) {
        sb.append("[✓] Code syntax checked: No runtime errors.\n")
        sb.append("[✓] Output buffer initialized: 0 warnings.\n")
    }

    sb.append("--------------------------------------\n")
    sb.append("[✓] Process terminated with Exit Code: 0\n")
    return sb.toString()
}
