package com.example.ui.apps

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VirtualFile

@Composable
fun NotepadApp(
    initialFile: VirtualFile? = null,
    onSaveFile: (String, String) -> Unit,
    onAskAiSummarize: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var content by remember {
        mutableStateOf(
            initialFile?.content ?: "# My Project Notes\n\n- Welcome to PC Desktop for Android!\n- Built-in Gemini Copilot for instant assistance.\n- Virtual C: drive storage for all your files.\n- Full app store with web apps & development tools.\n"
        )
    }
    var filePath by remember {
        mutableStateOf(initialFile?.path ?: "C:\\Users\\Admin\\Documents\\notes.txt")
    }

    val wordCount = remember(content) {
        if (content.isBlank()) 0 else content.trim().split(Regex("\\s+")).size
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Top Toolbar
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
                    imageVector = Icons.Default.Description,
                    contentDescription = "Notepad",
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = filePath.substringAfterLast('\\'),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // AI Summarize
                Button(
                    onClick = {
                        onAskAiSummarize("Please summarize and improve these notes:\n\n$content")
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
                    Text(text = "AI Polish", fontSize = 11.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Save Button
                Button(
                    onClick = {
                        onSaveFile(filePath, content)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Save", fontSize = 11.sp, color = Color.White)
                }
            }
        }

        // Text Editor Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF020617))
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF020617),
                    unfocusedContainerColor = Color(0xFF020617),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("notepad_text_input")
            )
        }

        // Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$wordCount words • ${content.length} characters",
                fontSize = 10.sp,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "UTF-8 • Windows (CRLF)",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}
