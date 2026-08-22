package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChannelAnalysis
import com.example.data.model.WifiBand
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueLight
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantGreenLight
import com.example.ui.theme.VibrantGreenSoft
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantRoseLight

@Composable
fun ChannelRatingTable(
    band: WifiBand,
    channels: List<ChannelAnalysis>,
    modifier: Modifier = Modifier
) {
    val bestChannels = channels.sortedByDescending { it.starRating }.take(3)
    val is24G = band == WifiBand.BAND_2_4_GHZ

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("channel_rating_card"),
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
                Text(
                    text = "Channel Rating & Recommendations",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Best Channel Banner
            val best = bestChannels.firstOrNull()
            if (best != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VibrantGreenSoft
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibrantGreenLight)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(VibrantGreenLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Best channel",
                                tint = VibrantGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Recommended Channel: CH ${best.channel}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibrantGreen
                            )
                            Text(
                                text = if (is24G) {
                                    "Channel ${best.channel} has the lowest co-channel interference (${best.networkCount} APs)."
                                } else {
                                    "Channel ${best.channel} is clear and offers wide uninterrupted bandwidth."
                                },
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Channels List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                channels.forEach { ch ->
                    ChannelRowItem(ch = ch)
                }
            }
        }
    }
}

@Composable
private fun ChannelRowItem(ch: ChannelAnalysis) {
    val (badgeBg, badgeText) = when {
        ch.starRating >= 4.2f -> Pair(VibrantGreenLight, VibrantGreen)
        ch.starRating >= 3.0f -> Pair(VibrantBlueLight, VibrantBlue)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = "CH ${ch.channel}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${ch.centerFreqMhz} MHz",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    Text(
                        text = if (ch.networkCount == 0) "0 APs (Clear)" else "${ch.networkCount} APs (Peak: ${ch.maxRssi ?: -90} dBm)",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                StarRatingBar(rating = ch.starRating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ch.ratingLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        ch.starRating >= 4.2f -> VibrantGreen
                        ch.starRating >= 3.0f -> Slate900
                        else -> VibrantOrange
                    }
                )
            }
        }
    }
}

@Composable
private fun StarRatingBar(rating: Float) {
    Row {
        for (i in 1..5) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VibrantAmber,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

