package com.example.signalfinder.scanner

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SignalHud() {
    var showTip by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentCarrier = "HOT Mobile"

    LaunchedEffect(Unit) {
        delay(4000)
        showTip = true
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        // Glassmorphism Main Info Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(Color(0x77000000), Color(0x33000000))))
                .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("5G N78 SA", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(currentCarrier, color = Color(0xFFB0BEC5), fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("ציון: 85", color = Color(0xFF00E676), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("RSRP: -85 dBm", color = Color.White, fontSize = 14.sp)
            }
        }
        
        if (showTip) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xDD003366), Color(0xDD005599))))
                    .border(1.dp, Color(0x8844AAFF), RoundedCornerShape(20.dp))
                    .clickable {
                        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=32.073581,34.788052")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Cyan, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("טיפ לשיפור המהירות", color = Color.Cyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "אנטנה פנויה של $currentCarrier נמצאת 300מ' מכאן. לחץ כאן לניווט מהיר במפה כדי להשיג מהירות טובה יותר.", 
                        color = Color.White, 
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
