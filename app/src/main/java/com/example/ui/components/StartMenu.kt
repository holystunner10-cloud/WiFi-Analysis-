package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCategory
import com.example.data.model.PcApp
import com.example.data.model.VirtualFile

@Composable
fun StartMenu(
    isOpen: Boolean,
    apps: List<PcApp>,
    recentFiles: List<VirtualFile>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onOpenApp: (String) -> Unit,
    onOpenFile: (VirtualFile) -> Unit,
    onOpenCopilotWithQuery: (String) -> Unit,
    onCloseMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    var selectedTab by remember { mutableStateOf(0) } // 0: Pinned, 1: All Apps, 2: Recent Files

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 60.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Surface(
            modifier = Modifier
                .width(360.dp)
                .heightIn(max = 520.dp)
                .shadow(elevation = 24.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(16.dp))
                .testTag("start_menu_surface"),
            color = Color(0xFA0B132B),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Search Input with Copilot trigger
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Search apps, files, or ask Holy Stunner AI...",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF60A5FA),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x221E293B),
                        unfocusedContainerColor = Color(0x111E293B),
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x3360A5FA),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("start_menu_search_input")
                )

                // If typing, show Quick AI Copilot action button
                if (searchQuery.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF1E1B4B), Color(0xFF1E293B))
                                )
                            )
                            .border(1.dp, Color(0x55818CF8), RoundedCornerShape(10.dp))
                            .clickable {
                                onOpenCopilotWithQuery(searchQuery)
                                onCloseMenu()
                            }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Copilot",
                            tint = Color(0xFFEC4899),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ask Holy Stunner AI: \"$searchQuery\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF93C5FD),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF3B82F6)
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "Pinned",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) Color(0xFF60A5FA) else Color(0xFF94A3B8)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "All Apps",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) Color(0xFF60A5FA) else Color(0xFF94A3B8)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Text(
                                text = "Recent Files",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) Color(0xFF60A5FA) else Color(0xFF94A3B8)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Content View based on Tab or Search Query
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val filteredApps = if (searchQuery.isBlank()) {
                        apps
                    } else {
                        apps.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.description.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    when {
                        searchQuery.isNotBlank() || selectedTab == 1 -> {
                            // All Apps List (Categorized)
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                items(filteredApps, key = { it.id }) { app ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onOpenApp(app.id)
                                                onCloseMenu()
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AppIconView(iconName = app.iconName, size = 32.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = app.name,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White
                                            )
                                            Text(
                                                text = app.description,
                                                fontSize = 11.sp,
                                                color = Color(0xFF94A3B8),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        selectedTab == 0 -> {
                            // Pinned Apps Grid
                            val pinnedApps = apps.filter { it.isPinnedDesktop || it.isPinnedTaskbar }
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                items(pinnedApps, key = { it.id }) { app ->
                                    Column(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onOpenApp(app.id)
                                                onCloseMenu()
                                            }
                                            .padding(vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AppIconView(iconName = app.iconName, size = 36.dp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = app.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        selectedTab == 2 -> {
                            // Recent Files List
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                val displayFiles = recentFiles.filter { !it.isDirectory }
                                items(displayFiles, key = { it.path }) { file ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onOpenFile(file)
                                                onCloseMenu()
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = "File",
                                            tint = Color(0xFF60A5FA),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = file.name,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White
                                            )
                                            Text(
                                                text = file.path,
                                                fontSize = 10.sp,
                                                color = Color(0xFF64748B),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Footer: User Profile & Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            0.5.dp,
                            Color(0x3360A5FA),
                            RoundedCornerShape(10.dp)
                        )
                        .background(Color(0x331E293B), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Profile Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(Color(0xFF2563EB), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Administrator",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Holy Stunner PC",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    // Settings & Store Shortcuts
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                onOpenApp("pc_store")
                                onCloseMenu()
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = "Store",
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                onOpenApp("settings")
                                onCloseMenu()
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
