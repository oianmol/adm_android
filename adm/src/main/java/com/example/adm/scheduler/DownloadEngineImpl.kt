package com.example.adm.scheduler

import com.example.adm.core.DownloadPromise
import com.example.adm.events.DownloadPromiseUpdater
import com.example.adm.events.DownloadStatus
import com.example.adm.http.HTTPDownloadInfo
import com.example.adm.http.HTTPDownloadStrategy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.*
import java.util.*
import kotlin.coroutines.suspendCoroutine

class DownloadEngineImpl : DownloadEngine() {

  override val okHttpClient = OkHttpClient()
  private val promises = hashMapOf<DownloadPromise, List<Job>?>()
  private val promisesFlow = MutableStateFlow(promises)
  override val promiseUpdateListeners = hashSetOf<DownloadPromiseUpdater>()
  override val downloadEngineScope = CoroutineScope(Job() + Dispatchers.IO)

  override suspend fun fetchInfoAndSchedule(url: String) = withContext(Dispatchers.IO) {
    val downloadItem = downloadInfoFromUrl(url)
    val promise = DownloadPromise(UUID.randomUUID().toString(), url, downloadItem, null)
    promises[promise] = null
    promiseUpdateListeners.forEach {
      it.promiseCreated(promise)
    }
    promisesFlow.tryEmit(promises)
    promise
  }

  override fun streamDownloadQueue() = promisesFlow

  override suspend fun downloadInfoFromUrl(url: String): HTTPDownloadInfo {
    val request = Request.Builder().url(url).build()
    return suspendCoroutine { continuation ->
      okhttpCall(request, url, continuation)
    }
  }

  override fun resumeDownloadsWithStratgey(httpDownloadStrategy: HTTPDownloadStrategy) {
    if (promises.isEmpty()) {
      // No files to download
      return
    }
    val downloadPromise = promises.entries.firstOrNull { canStartDownload(it) }
    downloadPromise?.let { promise ->
      val jobs = okhttpDownloadPromise(promise.key, httpDownloadStrategy, downloadEngineScope)
      promises[downloadPromise.key] = jobs

      while (true) {
        if (jobs.map { it.isCompleted }.size == jobs.size) {
          // all jbos are complete
          break;
        }
      }

      TODO("start the jobs to join the file parts")
    }
  }

  private fun canStartDownload(promiseJobsMap: MutableMap.MutableEntry<DownloadPromise, List<Job>?>) =
    promiseJobsMap.key.httpDownloadInfo.state() == DownloadStatus.infoProcessed && (promiseJobsMap.value.isNullOrEmpty())

}