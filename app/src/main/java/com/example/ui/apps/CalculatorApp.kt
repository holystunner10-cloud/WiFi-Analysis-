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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var clearOnNext by remember { mutableStateOf(false) }

    fun onDigit(d: String) {
        if (display == "0" || clearOnNext) {
            display = d
            clearOnNext = false
        } else {
            display += d
        }
    }

    fun onOp(op: String) {
        expression = "$display $op"
        clearOnNext = true
    }

    fun onEquals() {
        if (expression.isBlank()) return
        val parts = expression.split(" ")
        if (parts.size >= 2) {
            val num1 = parts[0].toDoubleOrNull() ?: 0.0
            val op = parts[1]
            val num2 = display.toDoubleOrNull() ?: 0.0
            val result = when (op) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else "Error"
                else -> 0.0
            }
            expression = "$expression $display ="
            display = if (result is Double) {
                if (result % 1.0 == 0.0) result.toLong().toString() else String.format("%.4f", result).trimEnd('0').trimEnd('.')
            } else result.toString()
            clearOnNext = true
        }
    }

    fun onClear() {
        display = "0"
        expression = ""
        clearOnNext = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
            .padding(10.dp)
    ) {
        // Calculation Tape & Main Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF020617))
                .border(1.dp, Color(0x3360A5FA), RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = display,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Calculator Buttons Grid
        val buttonRows = listOf(
            listOf("C" to Color(0xFFEF4444), "±" to Color(0xFF334155), "%" to Color(0xFF334155), "÷" to Color(0xFF2563EB)),
            listOf("7" to Color(0xFF1E293B), "8" to Color(0xFF1E293B), "9" to Color(0xFF1E293B), "×" to Color(0xFF2563EB)),
            listOf("4" to Color(0xFF1E293B), "5" to Color(0xFF1E293B), "6" to Color(0xFF1E293B), "-" to Color(0xFF2563EB)),
            listOf("1" to Color(0xFF1E293B), "2" to Color(0xFF1E293B), "3" to Color(0xFF1E293B), "+" to Color(0xFF2563EB)),
            listOf("0" to Color(0xFF1E293B), "." to Color(0xFF1E293B), "π" to Color(0xFF334155), "=" to Color(0xFF059669))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { (label, bg) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(8.dp))
                                .clickable {
                                    when (label) {
                                        "C" -> onClear()
                                        "±" -> display = if (display.startsWith("-")) display.drop(1) else "-$display"
                                        "%" -> {
                                            val v = display.toDoubleOrNull() ?: 0.0
                                            display = (v / 100.0).toString()
                                        }
                                        "π" -> display = "3.14159"
                                        "=" -> onEquals()
                                        "+", "-", "×", "÷" -> onOp(label)
                                        else -> onDigit(label)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
