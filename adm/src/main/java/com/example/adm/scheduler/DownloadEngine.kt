package com.example.adm.scheduler

import com.example.adm.core.DownloadPromise
import com.example.adm.events.DownloadPromiseUpdater
import com.example.adm.http.HTTPDownloadInfo
import com.example.adm.http.HTTPDownloadStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient

abstract class DownloadEngine {
  abstract suspend fun fetchInfoAndSchedule(url: String): DownloadPromise
  abstract fun streamDownloadQueue(): StateFlow<HashMap<DownloadPromise, List<Job>?>>
  abstract suspend fun downloadInfoFromUrl(url: String):HTTPDownloadInfo
  abstract fun resumeDownloadsWithStratgey(httpDownloadStrategy: HTTPDownloadStrategy)
  abstract val promiseUpdateListeners: HashSet<DownloadPromiseUpdater>
  abstract val okHttpClient: OkHttpClient
  abstract val downloadEngineScope: CoroutineScope
}