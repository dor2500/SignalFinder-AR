package com.example.signalfinder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalfinder.ar.ArOverlayView
import com.example.signalfinder.scanner.SignalHud
import com.example.signalfinder.speedtest.SpeedTestEngine
import com.example.signalfinder.speedtest.SpeedTestGauge
import com.example.signalfinder.updater.GitHubUpdater
import com.example.signalfinder.updater.UpdateInfo
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val coroutineScope = rememberCoroutineScope()
    val speedTestEngine = remember { SpeedTestEngine() }
    val testResult by speedTestEngine.result.collectAsState()
    
    // Toggle for Indoor (Wi-Fi) vs Outdoor (5G)
    var isIndoorMode by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }

    // Real OTA Update notification
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        updateInfo = GitHubUpdater.checkForUpdate()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ArOverlayView(modifier = Modifier.fillMaxSize(), isIndoorMode = isIndoorMode)
        
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
                .padding(top = 40.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Mode Switcher
            TabRow(
                selectedTabIndex = if (isIndoorMode) 1 else 0,
                containerColor = Color(0x66000000),
                contentColor = Color.Cyan,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = !isIndoorMode, 
                    onClick = { isIndoorMode = false }, 
                    text = { Text("בחוץ (5G)", fontWeight = FontWeight.Bold) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
                Tab(
                    selected = isIndoorMode, 
                    onClick = { isIndoorMode = true }, 
                    text = { Text("בבית (Wi-Fi)", fontWeight = FontWeight.Bold) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            SignalHud(isIndoorMode = isIndoorMode)
            
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
        updateInfo?.let { info ->
            AlertDialog(
                onDismissRequest = { updateInfo = null },
                containerColor = Color(0xFF222233),
                title = { Text("עדכון גרסה ${info.version} זמין! 🚀", color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text(info.releaseNotes, color = Color(0xFFCCCCCC)) },
                confirmButton = {
                    Button(
                        onClick = { 
                            GitHubUpdater.openDownloadUrl(context, info.downloadUrl)
                            updateInfo = null 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                    ) {
                        Text("הורד והתקן עכשיו", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { updateInfo = null }) {
                        Text("הזכר לי מאוחר יותר", color = Color.Gray)
                    }
                }
            )
        }
    }
}
