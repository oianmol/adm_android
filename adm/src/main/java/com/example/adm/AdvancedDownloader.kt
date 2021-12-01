package com.example.adm

import com.example.adm.core.DownloadManager
import com.example.adm.core.DownloadPromise
import com.example.adm.events.DownloadPromiseUpdater
import com.example.adm.core.DownloadManagerFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class AdvancedDownloader(
  private val downloadManager: DownloadManager
) {

  suspend fun processURL(url: String): DownloadPromise {
    return downloadManager.processURLInternal(url)
  }

  fun streamDownloadQueue(): StateFlow<HashMap<DownloadPromise, Job?>> {
    return downloadManager.streamDownloadQueue()
  }

  fun registerDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater) {
    downloadManager.addDownloadPromiseUpdater(downloadPromiseUpdater)
  }

  fun unregisterDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater) {
    downloadManager.removeDownloadPromiseUpdater(downloadPromiseUpdater)
  }

  fun resumeDownloads() {
    downloadManager.resumeDownloads()
  }

  companion object {
    fun newBuilder(): Builder {
      return Builder()
    }
  }

  class Builder {
    private var directory: File? = null
    var downloadManager: DownloadManager? = null

    fun build(): AdvancedDownloader {
      downloadManager?.let { safeDownloadEngine ->
        directory?.let { safeDirectory ->
          return AdvancedDownloader(safeDownloadEngine)
        } ?: run {
          throw RuntimeException("Directory can not be null")
        }
      } ?: run {
        throw RuntimeException("engine can not be null")
      }
    }

    fun setStorageFolder(file: File): Builder {
      directory = file
      return this
    }

    inline fun <reified T : DownloadManager> setDownloadManager(): Builder {
      downloadManager = DownloadManagerFactory.createFactory<T>().makeDownloadManager()
      return this
    }

  }

}

