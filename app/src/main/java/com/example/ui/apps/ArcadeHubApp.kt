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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

data class MineCell(
    val row: Int,
    val col: Int,
    var isMine: Boolean = false,
    var isRevealed: Boolean = false,
    var isFlagged: Boolean = false,
    var neighborMines: Int = 0
)

@Composable
fun ArcadeHubApp(modifier: Modifier = Modifier) {
    val rows = 8
    val cols = 8
    val totalMines = 8

    var isGameOver by remember { mutableStateOf(false) }
    var isWon by remember { mutableStateOf(false) }
    var isFlagMode by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(0) }

    val board = remember { mutableStateListOf<MineCell>() }

    fun resetBoard() {
        board.clear()
        val cells = mutableListOf<MineCell>()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                cells.add(MineCell(r, c))
            }
        }
        // Place mines randomly
        var placed = 0
        while (placed < totalMines) {
            val idx = Random.nextInt(cells.size)
            if (!cells[idx].isMine) {
                cells[idx].isMine = true
                placed++
            }
        }
        // Calculate neighbor mines
        for (cell in cells) {
            if (!cell.isMine) {
                var count = 0
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr == 0 && dc == 0) continue
                        val nr = cell.row + dr
                        val nc = cell.col + dc
                        if (nr in 0 until rows && nc in 0 until cols) {
                            val neighbor = cells.find { it.row == nr && it.col == nc }
                            if (neighbor?.isMine == true) count++
                        }
                    }
                }
                cell.neighborMines = count
            }
        }
        board.addAll(cells)
        isGameOver = false
        isWon = false
        timerSeconds = 0
    }

    LaunchedEffect(Unit) {
        resetBoard()
    }

    LaunchedEffect(isGameOver, isWon) {
        if (!isGameOver && !isWon) {
            while (true) {
                delay(1000)
                timerSeconds++
            }
        }
    }

    fun revealCell(cell: MineCell) {
        if (cell.isRevealed || cell.isFlagged || isGameOver || isWon) return

        if (cell.isMine) {
            cell.isRevealed = true
            isGameOver = true
            // reveal all mines
            board.forEach { if (it.isMine) it.isRevealed = true }
            return
        }

        cell.isRevealed = true

        // Flood fill empty neighbors
        if (cell.neighborMines == 0) {
            for (dr in -1..1) {
                for (dc in -1..1) {
                    val nr = cell.row + dr
                    val nc = cell.col + dc
                    if (nr in 0 until rows && nc in 0 until cols) {
                        val neighbor = board.find { it.row == nr && it.col == nc }
                        if (neighbor != null && !neighbor.isRevealed && !neighbor.isMine) {
                            revealCell(neighbor)
                        }
                    }
                }
            }
        }

        // Check Win
        val unrevealedSafe = board.count { !it.isMine && !it.isRevealed }
        if (unrevealedSafe == 0) {
            isWon = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Minesweeper Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = "Arcade",
                    tint = Color(0xFFF97316),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Classic Minesweeper PC",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Flag Mode Toggle
            Button(
                onClick = { isFlagMode = !isFlagMode },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFlagMode) Color(0xFFEF4444) else Color(0x331E293B)
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = "Flag",
                    tint = if (isFlagMode) Color.White else Color(0xFFEF4444),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isFlagMode) "Flag ON" else "Flag OFF",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Game Status Bar
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF020617))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Mines remaining
            Text(
                text = "💣 0$totalMines",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )

            // Smile face reset
            IconButton(
                onClick = { resetBoard() },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (isGameOver) Icons.Default.SentimentVeryDissatisfied else Icons.Default.SentimentSatisfied,
                    contentDescription = "Reset",
                    tint = if (isGameOver) Color(0xFFEF4444) else Color(0xFFFBBF24),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Timer
            Text(
                text = "⏱️ ${String.format("%03d", timerSeconds)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF60A5FA)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mine Grid
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E293B))
                .border(2.dp, Color(0x4460A5FA), RoundedCornerShape(6.dp))
                .padding(4.dp)
        ) {
            for (r in 0 until rows) {
                Row {
                    for (c in 0 until cols) {
                        val cell = board.find { it.row == r && it.col == c }
                        if (cell != null) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(1.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (cell.isRevealed) {
                                            if (cell.isMine) Color(0xFF7F1D1D) else Color(0xFF0F172A)
                                        } else {
                                            Color(0xFF334155)
                                        }
                                    )
                                    .border(0.5.dp, Color(0x4464748B), RoundedCornerShape(3.dp))
                                    .clickable {
                                        if (isFlagMode) {
                                            if (!cell.isRevealed) {
                                                cell.isFlagged = !cell.isFlagged
                                                // Trigger recompose
                                                board[board.indexOf(cell)] = cell.copy()
                                            }
                                        } else {
                                            revealCell(cell)
                                            // Trigger recompose
                                            board[board.indexOf(cell)] = cell.copy()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (cell.isRevealed) {
                                    if (cell.isMine) {
                                        Text(text = "💣", fontSize = 12.sp)
                                    } else if (cell.neighborMines > 0) {
                                        val col = when (cell.neighborMines) {
                                            1 -> Color(0xFF60A5FA)
                                            2 -> Color(0xFF4ADE80)
                                            3 -> Color(0xFFF87171)
                                            4 -> Color(0xFFA78BFA)
                                            else -> Color(0xFFFBBF24)
                                        }
                                        Text(
                                            text = "${cell.neighborMines}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = col
                                        )
                                    }
                                } else if (cell.isFlagged) {
                                    Text(text = "🚩", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isGameOver) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "💥 Game Over! Tap smiley to replay.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        } else if (isWon) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🎉 You Won! Excellent strategy!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4ADE80)
            )
        }
    }
}
