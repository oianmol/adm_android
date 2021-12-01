package com.example.adm.core

import com.example.adm.events.DownloadPromiseUpdater
import com.example.adm.scheduler.DownloadEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

abstract class DownloadManager {
  abstract var downloadEngine: DownloadEngine
  abstract fun addDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater)
  abstract fun removeDownloadPromiseUpdater(downloadPromiseUpdater: DownloadPromiseUpdater)
  abstract suspend fun processURLInternal(url: String): DownloadPromise
  abstract fun streamDownloadQueue(): StateFlow<HashMap<DownloadPromise, Job?>>
  abstract fun resumeDownloads()
}