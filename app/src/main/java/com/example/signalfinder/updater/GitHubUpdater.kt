package com.example.signalfinder.updater

import android.content.Context
import android.content.Intent
import android.net.Uri
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object GitHubUpdater {
    // Current version of the app is "1.0.0" so it triggers the "2.0.0" update
    const val CURRENT_VERSION = "1.0.0"
    private const val REPO_OWNER = "dor2500"
    private const val REPO_NAME = "SignalFinder-AR"

    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val client = HttpClient(OkHttp)
            val response: HttpResponse = client.get("https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/latest")
            val responseBody = response.bodyAsText()
            val json = JSONObject(responseBody)
            
            val latestVersion = json.getString("tag_name").removePrefix("v")
            val releaseNotes = json.getString("body")
            
            if (latestVersion != CURRENT_VERSION) {
                val assets = json.getJSONArray("assets")
                var downloadUrl = ""
                if (assets.length() > 0) {
                    downloadUrl = assets.getJSONObject(0).getString("browser_download_url")
                }
                return@withContext UpdateInfo(latestVersion, releaseNotes, downloadUrl)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    fun openDownloadUrl(context: Context, url: String) {
        if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }
}

data class UpdateInfo(
    val version: String,
    val releaseNotes: String,
    val downloadUrl: String
)
