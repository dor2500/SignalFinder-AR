package com.example.signalfinder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalfinder.ar.ArOverlayView
import com.example.signalfinder.scanner.SignalHud
import com.example.signalfinder.speedtest.SpeedTestEngine
import com.example.signalfinder.speedtest.SpeedTestGauge
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val coroutineScope = rememberCoroutineScope()
    val speedTestEngine = remember { SpeedTestEngine() }
    val testResult by speedTestEngine.result.collectAsState()
    
    var showUpdateDialog by remember { mutableStateOf(false) }

    // Simulating OTA Update notification
    LaunchedEffect(Unit) {
        delay(2500)
        showUpdateDialog = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ArOverlayView(modifier = Modifier.fillMaxSize())
        
        // Gradient overlay for UI readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent, Color.Black.copy(alpha = 0.95f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignalHud()
            
            Spacer(modifier = Modifier.weight(1f))

            if (testResult.isRunning || testResult.progress > 0f) {
                SpeedTestGauge(result = testResult)
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            FloatingActionButton(
                onClick = { 
                    if (!testResult.isRunning) {
                        coroutineScope.launch { speedTestEngine.runTest() }
                    }
                },
                containerColor = if (testResult.isRunning) Color.DarkGray else Color(0xFF00E676),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = if (testResult.isRunning) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = "Run Test",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = if (testResult.isRunning) "בודק מהירות..." else "התחל בדיקת מהירות",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        // In-App OTA Update Dialog
        if (showUpdateDialog) {
            AlertDialog(
                onDismissRequest = { showUpdateDialog = false },
                containerColor = Color(0xFF222233),
                title = { Text("עדכון גרסה זמין! 🚀", color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text("גרסה 2.0 של SignalFinder מוכנה. העדכון כולל ממשק משתמש חדש לגמרי, בדיקת מהירות שפועלת בזמן אמת, ותיקוני באגים.\nהורד עכשיו כדי ליהנות מהעיצוב החדש!", color = Color(0xFFCCCCCC)) },
                confirmButton = {
                    Button(
                        onClick = { showUpdateDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                    ) {
                        Text("התקן עדכון עכשיו", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUpdateDialog = false }) {
                        Text("הזכר לי מאוחר יותר", color = Color.Gray)
                    }
                }
            )
        }
    }
}
