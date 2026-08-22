package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScanNetworkItem
import com.example.data.model.SignalQuality
import com.example.data.model.WifiBand
import com.example.ui.SortOption
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueLight
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantGreenLight
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.VibrantRose

@Composable
fun NetworksScannerScreen(
    networks: List<ScanNetworkItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedBandFilter: WifiBand?,
    onBandFilterChange: (WifiBand?) -> Unit,
    sortOption: SortOption,
    onSortOptionChange: (SortOption) -> Unit,
    isScanning: Boolean,
    onTriggerScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search & Sort Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_network_input"),
                placeholder = { Text("Search SSID / BSSID...", fontSize = 14.sp, color = Slate400) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Slate400)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantBlue,
                    unfocusedBorderColor = LightBorder,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = Slate900,
                    unfocusedTextColor = Slate900
                )
            )

            Box {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
                    shadowElevation = 1.dp
                ) {
                    IconButton(
                        onClick = { showSortMenu = true },
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("btn_sort_menu")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort Networks",
                            tint = VibrantBlue
                        )
                    }
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label, fontWeight = FontWeight.Medium) },
                            onClick = {
                                onSortOptionChange(option)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Band Filter Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedBandFilter == null,
                    onClick = { onBandFilterChange(null) },
                    label = { Text("All (${networks.size})", fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibrantBlueLight,
                        selectedLabelColor = VibrantBlue,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = Slate500
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedBandFilter == null,
                        borderColor = LightBorder,
                        selectedBorderColor = VibrantBlue
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedBandFilter == WifiBand.BAND_2_4_GHZ,
                    onClick = { onBandFilterChange(WifiBand.BAND_2_4_GHZ) },
                    label = { Text("2.4 GHz", fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibrantBlueLight,
                        selectedLabelColor = VibrantBlue,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = Slate500
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedBandFilter == WifiBand.BAND_2_4_GHZ,
                        borderColor = LightBorder,
                        selectedBorderColor = VibrantBlue
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedBandFilter == WifiBand.BAND_5_GHZ,
                    onClick = { onBandFilterChange(WifiBand.BAND_5_GHZ) },
                    label = { Text("5 GHz", fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibrantBlueLight,
                        selectedLabelColor = VibrantBlue,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = Slate500
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedBandFilter == WifiBand.BAND_5_GHZ,
                        borderColor = LightBorder,
                        selectedBorderColor = VibrantBlue
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scan Results List
        if (networks.isEmpty()) {
            EmptyNetworksState(
                isScanning = isScanning,
                onTriggerScan = onTriggerScan
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(networks, key = { it.bssid + it.ssid }) { item ->
                    NetworkItemCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun NetworkItemCard(item: ScanNetworkItem) {
    var expanded by remember { mutableStateOf(false) }

    val signalColor = when (item.signalQuality) {
        SignalQuality.EXCELLENT -> VibrantGreen
        SignalQuality.GOOD -> VibrantBlue
        SignalQuality.FAIR -> VibrantAmber
        SignalQuality.POOR -> VibrantOrange
        SignalQuality.VERY_POOR -> VibrantRose
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("network_item_${item.ssid}"),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (item.isConnected) VibrantBlue else LightBorder
        ),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(signalColor.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Signal",
                            tint = signalColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.ssid,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            if (item.isConnected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = VibrantGreenLight
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VibrantGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.bssid,
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                // Signal Readout & Channel
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${item.rssi}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = signalColor
                        )
                        Text(
                            text = " dBm",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                    Text(
                        text = "CH ${item.channel} (${item.band.displayName})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VibrantBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metadata Chips: Security, Channel Width, Est. Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Security Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isOpen = item.capabilities.contains("Open", ignoreCase = true)
                        Icon(
                            imageVector = if (isOpen) Icons.Default.LockOpen else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isOpen) VibrantAmber else Slate500,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isOpen) "Open" else "Secured",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        )
                    }
                }

                // Estimated Distance Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = VibrantBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "~${item.distanceEstimateMeters} m",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        )
                    }
                }

                // Channel Width Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Text(
                        text = "${item.channelWidthMhz} MHz",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Slate400,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expanded Technical Details
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(Slate50, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DetailRow(label = "Exact Frequency", value = "${item.frequencyMhz} MHz")
                    DetailRow(label = "Channel Width", value = "${item.channelWidthMhz} MHz")
                    DetailRow(label = "Security Capabilities", value = item.capabilities)
                    DetailRow(label = "Estimated Proximity", value = "~${item.distanceEstimateMeters} meters")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Slate500)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
    }
}

@Composable
private fun EmptyNetworksState(
    isScanning: Boolean,
    onTriggerScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SignalCellularAlt,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Wi-Fi Networks Found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enable Location permissions or tap below to scan for nearby access points.",
            fontSize = 13.sp,
            color = Slate500,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onTriggerScan,
            colors = ButtonDefaults.buttonColors(
                containerColor = VibrantBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Scan",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Scan Networks", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

