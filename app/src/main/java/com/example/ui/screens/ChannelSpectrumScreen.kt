package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WifiChannel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChannelAnalysis
import com.example.data.model.ScanNetworkItem
import com.example.data.model.WifiBand
import com.example.ui.components.ChannelRatingTable
import com.example.ui.components.SpectrumGraph
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueLight
import com.example.ui.theme.VibrantBlueSoft

@Composable
fun ChannelSpectrumScreen(
    selectedBand: WifiBand,
    onSelectBand: (WifiBand) -> Unit,
    networks: List<ScanNetworkItem>,
    channels24G: List<ChannelAnalysis>,
    channels5G: List<ChannelAnalysis>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val activeChannels = if (selectedBand == WifiBand.BAND_2_4_GHZ) channels24G else channels5G

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Band Selector Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedBand == WifiBand.BAND_2_4_GHZ,
                onClick = { onSelectBand(WifiBand.BAND_2_4_GHZ) },
                label = { Text("2.4 GHz Spectrum", fontWeight = FontWeight.Bold) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.WifiChannel,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantBlueLight,
                    selectedLabelColor = VibrantBlue,
                    selectedLeadingIconColor = VibrantBlue,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = Slate500,
                    iconColor = Slate400
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedBand == WifiBand.BAND_2_4_GHZ,
                    borderColor = LightBorder,
                    selectedBorderColor = VibrantBlue
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chip_band_24")
            )

            FilterChip(
                selected = selectedBand == WifiBand.BAND_5_GHZ,
                onClick = { onSelectBand(WifiBand.BAND_5_GHZ) },
                label = { Text("5 GHz Spectrum", fontWeight = FontWeight.Bold) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.WifiChannel,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantBlueLight,
                    selectedLabelColor = VibrantBlue,
                    selectedLeadingIconColor = VibrantBlue,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = Slate500,
                    iconColor = Slate400
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedBand == WifiBand.BAND_5_GHZ,
                    borderColor = LightBorder,
                    selectedBorderColor = VibrantBlue
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chip_band_5")
            )
        }

        // Spectrum Graph Canvas
        SpectrumGraph(
            band = selectedBand,
            networks = networks
        )

        // Channel Star Rating Table & Best Channel Recommendations
        ChannelRatingTable(
            band = selectedBand,
            channels = activeChannels
        )

        // Educational Insight Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(VibrantBlueSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = VibrantBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (selectedBand == WifiBand.BAND_2_4_GHZ) "2.4 GHz Channel Best Practices" else "5 GHz Channel Best Practices",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedBand == WifiBand.BAND_2_4_GHZ) {
                            "In the 2.4 GHz band, channels overlap with adjacent channels. Only channels 1, 6, and 11 do not overlap with one another. Using other channels (like 3 or 8) causes severe interference across multiple frequencies."
                        } else {
                            "5 GHz channels offer significantly greater bandwidth with non-overlapping 20, 40, and 80 MHz channel widths. For mesh nodes, UNII-1 (36-48) and UNII-3 (149-165) provide zero radar-DFS restrictions."
                        },
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Slate500
                    )
                }
            }
        }
    }
}

