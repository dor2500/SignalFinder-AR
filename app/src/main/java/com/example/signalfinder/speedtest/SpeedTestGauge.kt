package com.example.signalfinder.speedtest

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpeedTestGauge(result: SpeedTestResult) {
    val maxSpeed = 800f
    val currentSpeed = if (result.progress < 0.5f) result.downloadMbps else result.uploadMbps
    val targetSweep = (currentSpeed / maxSpeed) * 270f
    
    val animatedSweep by animateFloatAsState(
        targetValue = targetSweep.coerceIn(0f, 270f),
        animationSpec = tween(durationMillis = 200), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color(0xDD1E1E2E), shape = RoundedCornerShape(24.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("בדיקת מהירות רשת", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.DarkGray.copy(alpha = 0.4f),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
                drawArc(
                    brush = Brush.sweepGradient(listOf(Color.Cyan, Color.Blue, Color(0xFFB000FF))),
                    startAngle = 135f,
                    sweepAngle = animatedSweep,
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.1f".format(currentSpeed),
                    fontSize = 42.sp, 
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = if (result.progress < 0.5f) "Download (Mbps)" else "Upload (Mbps)",
                    fontSize = 14.sp, 
                    color = Color(0xFFAAAAAA)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MetricItem("Ping", "${result.pingMs} ms", Color.Cyan)
            MetricItem("Down", "%.1f".format(result.downloadMbps), Color(0xFF00E676))
            MetricItem("Up", "%.1f".format(result.uploadMbps), Color(0xFFFF2A85))
        }
    }
}

@Composable
fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
