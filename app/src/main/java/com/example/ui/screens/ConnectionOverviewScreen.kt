package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiChannel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SignalHistoryPoint
import com.example.data.model.WifiConnectionInfo
import com.example.ui.components.SignalGauge
import com.example.ui.components.SignalTimeGraph
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueSoft
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantGreenLight
import com.example.ui.theme.VibrantGreenSoft
import com.example.ui.theme.VibrantIndigo
import com.example.ui.theme.VibrantIndigoSoft
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.VibrantOrangeSoft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConnectionOverviewScreen(
    connectionInfo: WifiConnectionInfo,
    signalHistory: List<SignalHistoryPoint>,
    onRunPing: () -> Unit,
    onViewChannels: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Connected Header Card
        ConnectedHeaderCard(connectionInfo = connectionInfo)

        // Live Signal Speedometer Gauge
        SignalGauge(
            rssi = connectionInfo.rssi,
            percentage = connectionInfo.signalPercentage,
            quality = connectionInfo.signalLevel
        )

        // Rolling Signal Stability Time-series Graph
        SignalTimeGraph(history = signalHistory)

        // Wi-Fi Health & Quality Score Card
        WifiHealthScoreCard(connectionInfo = connectionInfo)

        // Connection Specs Grid
        Text(
            text = "Connection Parameters",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900,
            modifier = Modifier.padding(top = 4.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SpecGridItem(
                icon = Icons.Default.WifiChannel,
                iconTint = VibrantBlue,
                iconBg = VibrantBlueSoft,
                title = "Band & Freq",
                value = "${connectionInfo.band.displayName}",
                subtitle = "${connectionInfo.frequencyMhz} MHz",
                modifier = Modifier.weight(1f)
            )
            SpecGridItem(
                icon = Icons.Default.Speed,
                iconTint = VibrantGreen,
                iconBg = VibrantGreenSoft,
                title = "Link Speed",
                value = "${connectionInfo.linkSpeedMbps} Mbps",
                subtitle = "Tx: ${connectionInfo.txLinkSpeedMbps} / Rx: ${connectionInfo.rxLinkSpeedMbps}",
                modifier = Modifier.weight(1f)
            )
            SpecGridItem(
                icon = Icons.Default.Router,
                iconTint = VibrantOrange,
                iconBg = VibrantOrangeSoft,
                title = "Channel",
                value = "CH ${connectionInfo.channelNumber}",
                subtitle = "${connectionInfo.channelWidthMhz} MHz Bandwidth",
                modifier = Modifier.weight(1f)
            )
            SpecGridItem(
                icon = Icons.Default.Lock,
                iconTint = VibrantIndigo,
                iconBg = VibrantIndigoSoft,
                title = "Security & Spec",
                value = connectionInfo.standard.substringBefore(" "),
                subtitle = connectionInfo.securityType,
                modifier = Modifier.weight(1f)
            )
        }

        // Network Addressing Card (IP, Gateway, DNS, Subnet)
        NetworkAddressingCard(
            connectionInfo = connectionInfo,
            onCopy = { label, value ->
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
                Toast.makeText(context, "Copied $label: $value", Toast.LENGTH_SHORT).show()
            }
        )

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRunPing,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_run_ping"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VibrantBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NetworkCheck,
                    contentDescription = "Diagnostics",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Test Latency", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onViewChannels,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_view_channels"),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, VibrantBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.WifiChannel,
                    contentDescription = "Channels",
                    tint = VibrantBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Spectrum", color = VibrantBlue, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ConnectedHeaderCard(connectionInfo: WifiConnectionInfo) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("connected_header_card"),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(VibrantBlueSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Connected Wi-Fi",
                        tint = VibrantBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = connectionInfo.ssid,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "BSSID: ${connectionInfo.bssid}",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = VibrantGreenLight
            ) {
                Text(
                    text = "CONNECTED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantGreen,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun WifiHealthScoreCard(connectionInfo: WifiConnectionInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Wi-Fi Quality Index",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        connectionInfo.healthScore >= 85 -> "Optimal signal stability & bandwidth."
                        connectionInfo.healthScore >= 65 -> "Good connection. Minor packet jitter."
                        else -> "Weak connection. Consider moving closer to AP."
                    },
                    fontSize = 12.sp,
                    color = Slate500
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp)
            ) {
                CircularProgressIndicator(
                    progress = { connectionInfo.healthScore / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = if (connectionInfo.healthScore >= 80) VibrantGreen else VibrantBlue,
                    trackColor = Slate200,
                    strokeWidth = 6.dp
                )
                Text(
                    text = "${connectionInfo.healthScore}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }
        }
    }
}

@Composable
private fun SpecGridItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate500
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Slate500
            )
        }
    }
}

@Composable
private fun NetworkAddressingCard(
    connectionInfo: WifiConnectionInfo,
    onCopy: (String, String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(VibrantIndigoSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lan,
                        contentDescription = "IP Addressing",
                        tint = VibrantIndigo,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Network Addressing & Routes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            AddressRow(
                label = "IP Address",
                value = connectionInfo.ipAddress,
                onCopy = { onCopy("IP Address", connectionInfo.ipAddress) }
            )
            AddressRow(
                label = "Gateway Router",
                value = connectionInfo.gatewayIp,
                onCopy = { onCopy("Gateway", connectionInfo.gatewayIp) }
            )
            AddressRow(
                label = "Subnet Mask",
                value = connectionInfo.subnetMask,
                onCopy = { onCopy("Subnet Mask", connectionInfo.subnetMask) }
            )
            AddressRow(
                label = "DNS Primary",
                value = connectionInfo.dns1,
                onCopy = { onCopy("DNS 1", connectionInfo.dns1) }
            )
            AddressRow(
                label = "DNS Secondary",
                value = connectionInfo.dns2,
                onCopy = { onCopy("DNS 2", connectionInfo.dns2) }
            )
            AddressRow(
                label = "Device MAC",
                value = connectionInfo.macAddress,
                onCopy = { onCopy("MAC", connectionInfo.macAddress) }
            )
        }
    }
}

@Composable
private fun AddressRow(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Slate500
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
            IconButton(
                onClick = onCopy,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy $label",
                    tint = Slate400,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

