package com.example.ui.apps

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCategory
import com.example.data.model.AppType
import com.example.data.model.PcApp
import com.example.ui.components.AppIconView

@Composable
fun PcAppStoreApp(
    apps: List<PcApp>,
    installedAndroidApps: List<PcApp>,
    onInstallApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onOpenApp: (String) -> Unit,
    onTogglePinDesktop: (String) -> Unit,
    onAddCustomWebApp: (String, String, AppCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String>("ALL") }
    var showAddCustomDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // App Store Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = "Store",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PC App Store & Packages",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Install desktop tools, web apps & utilities",
                        fontSize = 10.sp,
                        color = Color(0xFF60A5FA)
                    )
                }
            }

            // Install Custom Web App Button
            Button(
                onClick = { showAddCustomDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Add Web App",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Search Bar & Filter Chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search PC packages, developer tools, web apps...",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
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
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val categories = listOf("ALL", "DEVELOPMENT", "PRODUCTIVITY", "CREATIVE", "GAMES", "UTILITIES", "ANDROID")
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF2563EB) else Color(0x221E293B))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF60A5FA) else Color(0x2260A5FA),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // App Catalog List
        val allDisplayApps = if (selectedCategory == "ANDROID") {
            installedAndroidApps
        } else {
            apps
        }

        val filteredApps = allDisplayApps.filter { app ->
            val matchCat = when (selectedCategory) {
                "ALL" -> true
                "ANDROID" -> app.category == AppCategory.ANDROID
                "DEVELOPMENT" -> app.category == AppCategory.DEVELOPMENT
                "PRODUCTIVITY" -> app.category == AppCategory.PRODUCTIVITY
                "CREATIVE" -> app.category == AppCategory.CREATIVE
                "GAMES" -> app.category == AppCategory.GAMES
                "UTILITIES" -> app.category == AppCategory.UTILITIES
                else -> true
            }
            val matchQuery = if (searchQuery.isBlank()) true else {
                app.name.contains(searchQuery, ignoreCase = true) ||
                        app.description.contains(searchQuery, ignoreCase = true)
            }
            matchCat && matchQuery
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredApps, key = { it.id }) { app ->
                StoreAppCard(
                    app = app,
                    onInstall = { onInstallApp(app.id) },
                    onUninstall = { onUninstallApp(app.id) },
                    onOpen = { onOpenApp(app.id) },
                    onTogglePin = { onTogglePinDesktop(app.id) }
                )
            }
        }
    }

    // Add Custom Web App Dialog
    if (showAddCustomDialog) {
        AddCustomWebAppModal(
            onDismiss = { showAddCustomDialog = false },
            onConfirm = { name, url, category ->
                onAddCustomWebApp(name, url, category)
                showAddCustomDialog = false
            }
        )
    }
}

@Composable
fun StoreAppCard(
    app: PcApp,
    onInstall: () -> Unit,
    onUninstall: () -> Unit,
    onOpen: () -> Unit,
    onTogglePin: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIconView(
            iconName = app.iconName,
            size = 44.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = app.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0x333B82F6), RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = app.category.name,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF93C5FD)
                    )
                }
            }

            Text(
                text = app.description,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "By ${app.author} • ${app.downloadSizeMb} MB",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Action Buttons
        Column(horizontalAlignment = Alignment.End) {
            if (app.isInstalled) {
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = "Open",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                if (app.type != AppType.NATIVE) {
                    Spacer(modifier = Modifier.height(4.dp))
                    IconButton(
                        onClick = onUninstall,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Uninstall",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onInstall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF059669)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Install",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Install",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AddCustomWebAppModal(
    onDismiss: () -> Unit,
    onConfirm: (name: String, url: String, category: AppCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(AppCategory.PRODUCTIVITY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F172A),
        title = {
            Text(
                text = "Install Custom Web App",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Pin any web service as an independent desktop window (e.g. Spotify, Notion, GitHub, Wikipedia, etc.):",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("App Name (e.g. My Dashboard)", fontSize = 11.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x4460A5FA)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Web URL (e.g. https://...)", fontSize = 11.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x4460A5FA)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && url.isNotBlank()) {
                        onConfirm(name, url, selectedCat)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text("Install App", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        }
    )
}
