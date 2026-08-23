package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProcessInfo

@Composable
fun TaskManagerApp(
    cpuUsage: Float,
    ramUsageMb: Int,
    processes: List<ProcessInfo>,
    onKillProcess: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Performance Gauges Top Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // CPU Tile
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x331E293B))
                    .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "CPU Utilization", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "CPU",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${cpuUsage.toInt()}%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (cpuUsage > 70f) Color(0xFFEF4444) else Color(0xFF60A5FA)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { (cpuUsage / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF3B82F6),
                    trackColor = Color(0x223B82F6)
                )
            }

            // RAM Tile
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x331E293B))
                    .border(1.dp, Color(0x334ADE80), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Memory (RAM)", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = "RAM",
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${ramUsageMb} MB",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ADE80)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { (ramUsageMb / 2048f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF4ADE80),
                    trackColor = Color(0x224ADE80)
                )
            }
        }

        // Processes Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "PID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.width(44.dp))
            Text(text = "Process Name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.weight(1f))
            Text(text = "CPU%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.width(52.dp))
            Text(text = "RAM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.width(64.dp))
            Text(text = "Action", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.width(50.dp))
        }

        // Processes Table List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(processes, key = { it.pid }) { proc ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color(0x1160A5FA))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${proc.pid}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF64748B),
                        modifier = Modifier.width(44.dp)
                    )
                    Text(
                        text = proc.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${proc.cpuUsagePercent.toInt()}%",
                        fontSize = 11.sp,
                        color = Color(0xFF60A5FA),
                        modifier = Modifier.width(52.dp)
                    )
                    Text(
                        text = "${proc.memoryUsageMb} MB",
                        fontSize = 11.sp,
                        color = Color(0xFF4ADE80),
                        modifier = Modifier.width(64.dp)
                    )

                    if (proc.isKillable) {
                        Button(
                            onClick = { onKillProcess(proc.pid) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33EF4444)),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .size(width = 50.dp, height = 24.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text(text = "End", fontSize = 10.sp, color = Color(0xFFEF4444))
                        }
                    } else {
                        Text(
                            text = "Core",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.width(50.dp)
                        )
                    }
                }
            }
        }
    }
}
