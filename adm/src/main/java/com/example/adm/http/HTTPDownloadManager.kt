package com.example.adm.http

import com.example.adm.core.DownloadManager
import com.example.adm.core.DownloadPromise
import com.example.adm.events.DownloadPromiseUpdater
import com.example.adm.scheduler.DownloadEngine
import com.example.adm.scheduler.DownloadEngineImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import java.lang.RuntimeException

class HTTPDownloadManager : DownloadManager() {

  private var workers: Int = 1
  private var numberOfDownloads: Int = 1

  override var downloadEngine: DownloadEngine = DownloadEngineImpl()

  override fun addDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater) {
    downloadEngine.promiseUpdateListeners.add(downloadPromiseUpdater)
  }

  override fun removeDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater) {
    downloadEngine.promiseUpdateListeners.remove(downloadPromiseUpdater)
  }

  override suspend fun processURLInternal(url: String): DownloadPromise {
    return downloadEngine.fetchInfoAndSchedule(url)
  }

  override fun streamDownloadQueue(): StateFlow<HashMap<DownloadPromise, Job?>> {
    return downloadEngine.streamDownloadQueue()
  }

  fun setSimultaneousDownloads(count: Int) {
    numberOfDownloads = count
  }

  fun setWorkersPerDownload(workers: Int) {
    if (workers > 0 && workers <= Runtime.getRuntime().availableProcessors()) {
      this.workers = workers
    } else {
      throw RuntimeException("workers > 0 && workers <= Runtime.getRuntime().availableProcessors() not satisfied.")
    }
  }

  override fun resumeDownloads() {
    downloadEngine.resumeDownloadsWithStratgey(
      HTTPDownloadStrategy(
        downloads = numberOfDownloads,
        workers = workers
      )
    )
  }
}