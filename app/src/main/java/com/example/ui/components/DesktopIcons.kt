package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PcApp

@Composable
fun DesktopIconsGrid(
    apps: List<PcApp>,
    selectedAppId: String?,
    onSelectApp: (String?) -> Unit,
    onOpenApp: (String) -> Unit,
    onLongClickApp: (PcApp) -> Unit,
    modifier: Modifier = Modifier
) {
    val pinnedApps = apps.filter { it.isPinnedDesktop && it.isInstalled }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 76.dp),
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .padding(top = 16.dp, start = 12.dp, bottom = 72.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(pinnedApps, key = { it.id }) { app ->
            DesktopIconItem(
                app = app,
                isSelected = selectedAppId == app.id,
                onClick = {
                    if (selectedAppId == app.id) {
                        onOpenApp(app.id)
                    } else {
                        onSelectApp(app.id)
                    }
                },
                onDoubleClick = {
                    onOpenApp(app.id)
                },
                onLongClick = {
                    onSelectApp(app.id)
                    onLongClickApp(app)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DesktopIconItem(
    app: PcApp,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = 74.dp, height = 86.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0x553B82F6) else Color.Transparent)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color(0x9993C5FD) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag("desktop_icon_${app.id}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIconView(
            iconName = app.iconName,
            size = 42.dp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = app.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            modifier = Modifier.shadow(elevation = 2.dp)
        )
    }
}
