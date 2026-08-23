package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.data.model.WindowState
import kotlin.math.roundToInt

@Composable
fun WindowFrame(
    window: WindowState,
    isActive: Boolean,
    onFocus: () -> Unit,
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    onToggleMaximize: () -> Unit,
    onMove: (Float, Float) -> Unit,
    onResize: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (window.isMinimized) return

    val shape = if (window.isMaximized) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp)
    val borderColor = if (isActive) Color(0xFF3B82F6) else Color(0xFF334155)
    val borderWidth = if (isActive) 1.5.dp else 1.dp

    Box(
        modifier = modifier
            .zIndex(window.zIndex)
            .then(
                if (window.isMaximized) {
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 54.dp)
                } else {
                    Modifier
                        .offset { IntOffset(window.offsetX.roundToInt(), window.offsetY.roundToInt()) }
                        .size(width = window.widthDp.dp, height = window.heightDp.dp)
                }
            )
            .shadow(
                elevation = if (isActive) 16.dp else 6.dp,
                shape = shape
            )
            .clip(shape)
            .background(Color(0xFF0F172A))
            .border(borderWidth, borderColor, shape)
            .clickable { onFocus() }
            .testTag("window_${window.appId}")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Window Titlebar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .background(if (isActive) Color(0xFF1E293B) else Color(0xFF0B132B))
                    .pointerInput(window.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onMove(dragAmount.x / density, dragAmount.y / density)
                        }
                    }
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AppIconView(
                        iconName = window.iconName,
                        size = 20.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = window.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isActive) Color.White else Color(0xFF94A3B8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Window Control Buttons (Minimize, Maximize, Close)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Minimize
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onMinimize() }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Minimize",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Maximize / Restore
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onToggleMaximize() }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (window.isMaximized) Icons.Default.FilterNone else Icons.Default.CropSquare,
                            contentDescription = "Maximize",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Close
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onClose() }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Window Client Area / Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF020617))
            ) {
                content()
            }
        }

        // Window Resize Corner Handle (when not maximized)
        if (!window.isMaximized) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .pointerInput(window.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onResize(dragAmount.x / density, dragAmount.y / density)
                        }
                    }
            )
        }
    }
}
