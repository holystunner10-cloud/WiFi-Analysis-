package com.example.ui.apps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender

@Composable
fun AiCopilotApp(
    messages: List<ChatMessage>,
    isThinking: Boolean,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onOpenCodeStudioWithCode: (String) -> Unit,
    initialQuery: String? = null,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf(initialQuery ?: "") }
    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(initialQuery) {
        if (!initialQuery.isNullOrBlank()) {
            onSendMessage(initialQuery)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // App Header Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFFEC4899))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Gemini",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Holy Stunner AI Studio",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Built-in AI Desktop Assistant • Holy Stunner PC",
                        fontSize = 10.sp,
                        color = Color(0xFF60A5FA)
                    )
                }
            }

            IconButton(
                onClick = onClearChat,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CleaningServices,
                    contentDescription = "Clear Chat",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatMessageBubble(
                    message = msg,
                    onCopy = { text ->
                        clipboardManager.setText(AnnotatedString(text))
                    },
                    onOpenInCodeStudio = { code ->
                        onOpenCodeStudioWithCode(code)
                    }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFF818CF8),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Holy Stunner AI is thinking...",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Suggested Prompt Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf(
                "⚡ Ask Holy Stunner AI to code",
                "🖥️ Show Holy Stunner PC specs",
                "💻 Write Python automation script",
                "📝 Draft project readme",
                "📦 How to install web apps"
            )
            items(suggestions) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x331E293B))
                        .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(12.dp))
                        .clickable { onSendMessage(prompt) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        color = Color(0xFF93C5FD)
                    )
                }
            }
        }

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = "Ask Holy Stunner AI anything...",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x221E293B),
                    unfocusedContainerColor = Color(0x111E293B),
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0x3360A5FA),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("copilot_input_field")
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF6366F1))),
                        CircleShape
                    )
                    .testTag("btn_copilot_send")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    onCopy: (String) -> Unit,
    onOpenInCodeStudio: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 14.dp
                    )
                )
                .background(
                    if (isUser) {
                        Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
                    }
                )
                .border(
                    1.dp,
                    if (isUser) Color(0x4460A5FA) else Color(0x3360A5FA),
                    RoundedCornerShape(14.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "AI",
                            tint = Color(0xFFEC4899),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Holy Stunner AI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEC4899)
                        )
                    }
                }

                Text(
                    text = message.text,
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 16.sp
                )

                // If message contains code snippet
                if (message.text.contains("```")) {
                    val codeContent = extractCodeBlock(message.text)
                    if (codeContent.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2563EB))
                                    .clickable { onOpenInCodeStudio(codeContent) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Run",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Open in Code Studio",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x33FFFFFF))
                                    .clickable { onCopy(codeContent) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Copy Code",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun extractCodeBlock(rawText: String): String {
    val start = rawText.indexOf("```")
    if (start == -1) return ""
    val secondLine = rawText.indexOf('\n', start)
    if (secondLine == -1) return ""
    val end = rawText.indexOf("```", secondLine)
    if (end == -1) return rawText.substring(secondLine + 1)
    return rawText.substring(secondLine + 1, end).trim()
}
