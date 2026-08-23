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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Html
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ViewList
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
import com.example.data.model.VirtualFile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FileExplorerApp(
    onOpenFileInNotepad: (VirtualFile) -> Unit,
    onOpenFileInCodeStudio: (VirtualFile) -> Unit,
    onOpenTerminalWithPath: (String) -> Unit,
    listFiles: (String) -> List<VirtualFile>,
    onCreateFile: (String, String, Boolean, String) -> Boolean,
    onDeleteFile: (String) -> Boolean,
    initialPath: String = "C:\\Users\\Admin",
    modifier: Modifier = Modifier
) {
    var currentPath by remember { mutableStateOf(initialPath) }
    var selectedFile by remember { mutableStateOf<VirtualFile?>(null) }
    var isGridView by remember { mutableStateOf(false) }

    var showNewFolderDialog by remember { mutableStateOf(false) }
    var showNewFileDialog by remember { mutableStateOf(false) }

    val files = remember(currentPath) { listFiles(currentPath) }

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
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation Buttons (Up Level)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (currentPath.contains("\\")) {
                            val parent = currentPath.substringBeforeLast('\\', "C:")
                            currentPath = if (parent.isBlank()) "C:" else parent
                            selectedFile = null
                        }
                    },
                    enabled = currentPath != "C:",
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Up",
                        tint = if (currentPath != "C:") Color(0xFF60A5FA) else Color(0xFF475569),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Breadcrumb Path Display
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x221E293B))
                        .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = currentPath,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF93C5FD),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Action Buttons (New Folder, New File, View Toggle, Terminal)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showNewFolderDialog = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CreateNewFolder,
                        contentDescription = "New Folder",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { showNewFileDialog = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NoteAdd,
                        contentDescription = "New File",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { onOpenTerminalWithPath(currentPath) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal Here",
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { isGridView = !isGridView },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = "Toggle View",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (selectedFile != null && selectedFile?.path != "C:" && selectedFile?.path != "C:\\Users") {
                    IconButton(
                        onClick = {
                            selectedFile?.let {
                                onDeleteFile(it.path)
                                selectedFile = null
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Quick Navigation Sidebar / Drives Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x221E293B))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Drive C:\\ (Virtual OS Disk) • ${files.size} items",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Desktop", "Documents", "Projects", "Downloads").forEach { folder ->
                    Text(
                        text = folder,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF60A5FA),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                currentPath = "C:\\Users\\Admin\\$folder"
                                selectedFile = null
                            }
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }

        // Files List or Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(6.dp)
        ) {
            if (files.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "This folder is empty",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(files, key = { it.path }) { file ->
                        val isSelected = selectedFile?.path == file.path
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0x443B82F6) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF60A5FA) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (isSelected) {
                                        if (file.isDirectory) {
                                            currentPath = file.path
                                            selectedFile = null
                                        } else {
                                            openFile(file, onOpenFileInNotepad, onOpenFileInCodeStudio)
                                        }
                                    } else {
                                        selectedFile = file
                                    }
                                }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FileIcon(file = file, size = 36.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = file.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(files, key = { it.path }) { file ->
                        val isSelected = selectedFile?.path == file.path
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0x443B82F6) else Color.Transparent)
                                .border(
                                    0.5.dp,
                                    if (isSelected) Color(0xFF60A5FA) else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    if (isSelected) {
                                        if (file.isDirectory) {
                                            currentPath = file.path
                                            selectedFile = null
                                        } else {
                                            openFile(file, onOpenFileInNotepad, onOpenFileInCodeStudio)
                                        }
                                    } else {
                                        selectedFile = file
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FileIcon(file = file, size = 20.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = file.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (file.isDirectory) "Folder" else "${file.sizeBytes} B",
                                fontSize = 10.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(file.lastModified)),
                                fontSize = 10.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }
    }

    // New Folder Dialog
    if (showNewFolderDialog) {
        var folderName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showNewFolderDialog = false },
            containerColor = Color(0xFF0F172A),
            title = { Text("Create New Folder", color = Color.White, fontSize = 15.sp) },
            text = {
                OutlinedTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    placeholder = { Text("Folder Name", color = Color(0xFF64748B)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x4460A5FA)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (folderName.isNotBlank()) {
                            onCreateFile(currentPath, folderName.trim(), true, "")
                            showNewFolderDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Create", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewFolderDialog = false }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            }
        )
    }

    // New File Dialog
    if (showNewFileDialog) {
        var fileName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showNewFileDialog = false },
            containerColor = Color(0xFF0F172A),
            title = { Text("Create New File", color = Color.White, fontSize = 15.sp) },
            text = {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    placeholder = { Text("e.g. script.py, notes.txt", color = Color(0xFF64748B)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x4460A5FA)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (fileName.isNotBlank()) {
                            onCreateFile(currentPath, fileName.trim(), false, "")
                            showNewFileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Create File", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewFileDialog = false }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            }
        )
    }
}

@Composable
fun FileIcon(file: VirtualFile, size: androidx.compose.ui.unit.Dp) {
    if (file.isDirectory) {
        Icon(
            imageVector = Icons.Default.Folder,
            contentDescription = "Folder",
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(size)
        )
    } else {
        val (icon, tint) = when (file.extension.lowercase()) {
            "py", "js", "ts", "kt", "cpp" -> Pair(Icons.Default.Code, Color(0xFF60A5FA))
            "html", "htm", "css" -> Pair(Icons.Default.Html, Color(0xFFF97316))
            "md", "txt", "doc" -> Pair(Icons.Default.Description, Color(0xFF93C5FD))
            else -> Pair(Icons.Default.Description, Color(0xFF94A3B8))
        }
        Icon(
            imageVector = icon,
            contentDescription = file.extension,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}

private fun openFile(
    file: VirtualFile,
    onOpenInNotepad: (VirtualFile) -> Unit,
    onOpenInCodeStudio: (VirtualFile) -> Unit
) {
    when (file.extension.lowercase()) {
        "py", "js", "html", "kt", "json" -> onOpenInCodeStudio(file)
        else -> onOpenInNotepad(file)
    }
}
