package com.example.advanceddownloadmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adm.AdvancedDownloader
import com.example.adm.http.HTTPDownloadManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val appDownloadPromiseUpdater = AppDownloadPromiseUpdater()

    val advancedDownloader = AdvancedDownloader.newBuilder()
      .setStorageFolder(this.filesDir)
      .setDownloadManager<HTTPDownloadManager>()
      .build()
    advancedDownloader.registerDownloadPromiseUpdater(appDownloadPromiseUpdater)
    advancedDownloader.unregisterDownloadPromiseUpdater(appDownloadPromiseUpdater)
    advancedDownloader.streamDownloadQueue()

    GlobalScope.launch {
      advancedDownloader.processURL("https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Logo_SDF.jpg/440px-Logo_SDF.jpg")
      advancedDownloader.resumeDownloads()
    }
  }
}