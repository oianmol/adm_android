package com.example.adm.scheduler

import com.example.adm.core.DownloadPromise
import com.example.adm.http.DownloadPart
import com.example.adm.http.HTTPDownloadStrategy
import com.example.adm.http.generateParts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

const val BUFFER_SIZE: Long = (500 * 1024).toLong()

fun DownloadEngine.okhttpDownloadPromise(
  downloadPromise: DownloadPromise,
  httpDownloadStrategy: HTTPDownloadStrategy,
  coroutineScope: CoroutineScope
): List<Job> {
  val parts = generateParts(downloadPromise.httpDownloadInfo, httpDownloadStrategy)
  return parts.map { part ->
    launchJobForDownloadPart(coroutineScope, downloadPromise, part)
  }
}

private fun DownloadEngine.launchJobForDownloadPart(
  coroutineScope: CoroutineScope,
  downloadPromise: DownloadPromise,
  part: DownloadPart
): Job {
  return coroutineScope.launch {
    val request = Request.Builder().url(downloadPromise.url)
    request.addHeader("Range", "bytes=" + part.startRange + "-" + part.endRange)
    okHttpClient.newCall(request.build()).enqueue(processDownloadPartCallback(part))
  }
}

private fun processDownloadPartCallback(part: DownloadPart) =
  object : Callback {
    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
      whenStreamingBodyAvailable(response, part.file)
    }
  }

private fun whenStreamingBodyAvailable(response: Response, file: File) {
  val source = response.body?.source()
  val contentLength = response.body?.contentLength()
  val bufferedSink = file.sink(append = true).buffer()
  source?.let {
    var totalRead: Long = 0
    var read: Long = 0
    while (source.read(bufferedSink.buffer, BUFFER_SIZE)
        .also { read = it } != (-1).toLong()
    ) {
      totalRead += read
      contentLength?.let {
        val progress = (totalRead * 100) / contentLength
        TODO("do something with this progress")
      } ?: run {
        TODO("we don't know the length of this response body ! show indefinite progress bar")
      }
    }
  }
}