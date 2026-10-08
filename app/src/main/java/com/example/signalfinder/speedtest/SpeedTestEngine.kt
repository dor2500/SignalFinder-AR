package com.example.signalfinder.speedtest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class SpeedTestResult(
    val downloadMbps: Float,
    val uploadMbps: Float,
    val pingMs: Int,
    val isRunning: Boolean,
    val progress: Float // 0f to 1f
)

class SpeedTestEngine {
    private val _result = MutableStateFlow(SpeedTestResult(0f, 0f, 0, false, 0f))
    val result: StateFlow<SpeedTestResult> = _result

    suspend fun runTest() {
        _result.value = SpeedTestResult(0f, 0f, 0, true, 0f)
        
        // 1. Ping Phase
        delay(600)
        val ping = Random.nextInt(12, 45)
        _result.value = _result.value.copy(pingMs = ping, progress = 0.1f)
        
        // 2. Download Test (Simulated Animation)
        var currentDownload = 0f
        for (i in 1..40) {
            currentDownload += Random.nextFloat() * 25f
            if (currentDownload > 600f) currentDownload -= Random.nextFloat() * 15f
            _result.value = _result.value.copy(
                downloadMbps = currentDownload,
                progress = 0.1f + (i / 40f) * 0.4f
            )
            delay(80)
        }
        val finalDownload = currentDownload
        
        // 3. Upload Test (Simulated Animation)
        var currentUpload = 0f
        for (i in 1..40) {
            currentUpload += Random.nextFloat() * 6f
            if (currentUpload > 120f) currentUpload -= Random.nextFloat() * 5f
            _result.value = _result.value.copy(
                uploadMbps = currentUpload,
                progress = 0.5f + (i / 40f) * 0.5f
            )
            delay(80)
        }
        
        // Done
        _result.value = SpeedTestResult(finalDownload, currentUpload, ping, false, 1f)
    }
}
