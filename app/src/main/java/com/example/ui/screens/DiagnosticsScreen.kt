package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanDevice
import com.example.data.model.PingResult
import com.example.data.model.WifiConnectionInfo
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantAmberLight
import com.example.ui.theme.VibrantAmberSoft
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueLight
import com.example.ui.theme.VibrantBlueSoft
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantGreenLight
import com.example.ui.theme.VibrantIndigo
import com.example.ui.theme.VibrantIndigoSoft
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantRoseLight

@Composable
fun DiagnosticsScreen(
    connectionInfo: WifiConnectionInfo,
    pingResults: List<PingResult>,
    lanDevices: List<LanDevice>,
    isLanScanning: Boolean,
    lanScanProgress: Float,
    onRunPing: () -> Unit,
    onScanLan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Multi-Target Latency & Ping Card
        PingDiagnosticsCard(
            pingResults = pingResults,
            onRunPing = onRunPing
        )

        // LAN Subnet Device Scanner Card
        LanScannerCard(
            devices = lanDevices,
            isScanning = isLanScanning,
            progress = lanScanProgress,
            onScan = onScanLan
        )

        // Wi-Fi Health & Optimization Advisory
        HealthAdvisoryCard(connectionInfo = connectionInfo)
    }
}

@Composable
private fun PingDiagnosticsCard(
    pingResults: List<PingResult>,
    onRunPing: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ping_diagnostics_card"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(VibrantBlueSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Ping",
                            tint = VibrantBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Real-Time Latency & Jitter",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Button(
                    onClick = onRunPing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VibrantBlueLight,
                        contentColor = VibrantBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Re-test", color = VibrantBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (pingResults.isEmpty()) {
                Text(
                    text = "Tap 'Re-test' to benchmark latency against gateway and public DNS servers.",
                    fontSize = 12.sp,
                    color = Slate500
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pingResults.forEach { res ->
                        PingItemRow(res = res)
                    }
                }
            }
        }
    }
}

@Composable
private fun PingItemRow(res: PingResult) {
    val (badgeBg, latencyColor) = when {
        res.latencyMs < 20 -> Pair(VibrantGreenLight, VibrantGreen)
        res.latencyMs < 60 -> Pair(VibrantBlueLight, VibrantBlue)
        res.latencyMs < 120 -> Pair(VibrantAmberLight, VibrantAmber)
        else -> Pair(VibrantRoseLight, VibrantRose)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = res.targetName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "${res.targetHost} • Jitter ~${res.jitterMs}ms",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = badgeBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(latencyColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${res.latencyMs} ms",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = latencyColor
                    )
                }
            }
        }
    }
}

@Composable
private fun LanScannerCard(
    devices: List<LanDevice>,
    isScanning: Boolean,
    progress: Float,
    onScan: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lan_scanner_card"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(VibrantIndigoSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "LAN",
                            tint = VibrantIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LAN Subnet Devices (${devices.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Button(
                    onClick = onScan,
                    enabled = !isScanning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VibrantBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Scan", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            AnimatedVisibility(visible = isScanning) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = VibrantBlue,
                        trackColor = Slate200
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Probing subnet IP hosts (${(progress * 100).toInt()}%)...",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (devices.isEmpty()) {
                Text(
                    text = "Tap 'Scan' to discover active laptops, smart TVs, IoT gateways, and storage servers on your local Wi-Fi.",
                    fontSize = 12.sp,
                    color = Slate500
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    devices.forEach { dev ->
                        LanDeviceRow(dev = dev)
                    }
                }
            }
        }
    }
}

@Composable
private fun LanDeviceRow(dev: LanDevice) {
    val deviceIcon = when {
        dev.isGateway -> Icons.Default.Router
        dev.deviceType.contains("TV", ignoreCase = true) -> Icons.Default.SmartDisplay
        dev.deviceType.contains("Speaker", ignoreCase = true) -> Icons.Default.Speaker
        dev.deviceType.contains("Storage", ignoreCase = true) || dev.deviceType.contains("NAS", ignoreCase = true) -> Icons.Default.Storage
        dev.deviceType.contains("Workstation", ignoreCase = true) || dev.deviceType.contains("Laptop", ignoreCase = true) -> Icons.Default.Computer
        else -> Icons.Default.Sensors
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (dev.isGateway) VibrantGreenLight
                            else if (dev.isSelf) VibrantBlueLight
                            else Slate200,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = deviceIcon,
                        contentDescription = null,
                        tint = if (dev.isGateway) VibrantGreen else if (dev.isSelf) VibrantBlue else Slate500,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = dev.hostname,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        if (dev.isSelf) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(6.dp), color = VibrantBlueLight) {
                                Text(
                                    text = "YOU",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VibrantBlue,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "${dev.ip} • ${dev.deviceType}",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            Text(
                text = "${dev.responseTimeMs}ms",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantGreen
            )
        }
    }
}

@Composable
private fun HealthAdvisoryCard(connectionInfo: WifiConnectionInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(VibrantAmberSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Tips",
                        tint = VibrantAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Wi-Fi Health & Optimization Tips",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            AdvisoryItem(
                icon = Icons.Default.CheckCircle,
                iconColor = VibrantGreen,
                title = "Channel Selection",
                description = "For 2.4 GHz, set router channels strictly to 1, 6, or 11 to avoid partial wave overlap with neighboring APs."
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdvisoryItem(
                icon = Icons.Default.CheckCircle,
                iconColor = VibrantBlue,
                title = "Band Steering & 5 GHz",
                description = "5 GHz provides ~3-4x faster throughput and zero microwave/Bluetooth interference. Use 5 GHz for video calls and gaming."
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdvisoryItem(
                icon = Icons.Default.Warning,
                iconColor = VibrantAmber,
                title = "Router Placement",
                description = "Elevate router 1-1.5 meters above the floor and avoid placing it inside metal cabinets or behind dense concrete walls."
            )
        }
    }
}

@Composable
private fun AdvisoryItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Slate50,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = Slate500
                )
            }
        }
    }
}

