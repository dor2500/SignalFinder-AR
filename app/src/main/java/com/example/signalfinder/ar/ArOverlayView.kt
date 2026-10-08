package com.example.signalfinder.ar

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun ArOverlayView(modifier: Modifier = Modifier, isIndoorMode: Boolean = false) {
    Box(modifier = modifier) {
        CameraPreviewView(modifier = Modifier.fillMaxSize())
        NavigationArrowOverlay(isIndoorMode)
    }
}

@Composable
fun CameraPreviewView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

@Composable
fun NavigationArrowOverlay(isIndoorMode: Boolean) {
    var angle by remember { mutableStateOf(0f) }
    var distance by remember { mutableStateOf(15.0f) }
    
    LaunchedEffect(isIndoorMode) {
        distance = if (isIndoorMode) 8.0f else 300.0f
    }
    
    LaunchedEffect(Unit) {
        var time = 0f
        while (true) {
            angle = (sin(time) * 30f)
            distance = maxOf(0f, distance - (if(distance > 20f) 5f else 0.2f))
            time += 0.1f
            delay(100)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(modifier = Modifier.size(160.dp)) {
                rotate(degrees = angle) {
                    val path = Path().apply {
                        moveTo(size.width / 2f, 0f)
                        lineTo(size.width, size.height)
                        lineTo(size.width / 2f, size.height * 0.7f)
                        lineTo(0f, size.height)
                        close()
                    }
                    val color = if (distance < 5f) Color.Green else Color(0xFFFFCC00)
                    drawPath(path, color = color.copy(alpha = 0.85f))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val targetName = if (isIndoorMode) "לראוטר האלחוטי" else "לאנטנה הסלולרית"
            
            Text(
                text = if (distance < 1f) "הגעת לנקודה הטובה ביותר!" else "כוון $targetName\nמרחק: ${"%.1f".format(distance)} מטרים",
                color = if (distance < 5f) Color.Green else Color(0xFFFFCC00),
                fontSize = 24.sp,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 12f)
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
