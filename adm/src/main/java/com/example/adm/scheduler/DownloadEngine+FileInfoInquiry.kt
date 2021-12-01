package com.example.adm.scheduler

import com.example.adm.events.DownloadStatus
import com.example.adm.http.HTTPDownloadInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.headersContentLength
import java.io.IOException
import kotlin.coroutines.Continuation


fun DownloadEngine.okhttpCall(
  request: Request,
  url: String,
  continuation: Continuation<HTTPDownloadInfo>
) {
  okHttpClient.newCall(request).enqueue(object : Callback {
    override fun onResponse(call: Call, response: Response) {
      val item = httpSuccessDownloadItem(url, response)
      continuation.resumeWith(Result.success(item))
    }

    override fun onFailure(call: Call, e: IOException) {
      val item = httpFailedDownloadItem(url, e)
      continuation.resumeWith(Result.success(item))
    }
  })
}

fun httpFailedDownloadItem(
  url: String,
  e: IOException
): HTTPDownloadInfo {
  return HTTPDownloadInfo(
    url,
    "unknown",
    -1,
    DownloadStatus.Failed(e)
  )
}

fun httpSuccessDownloadItem(
  url: String,
  response: Response
): HTTPDownloadInfo {
  return HTTPDownloadInfo(
    url,
    response.header("content-type", "application/*")!!,
    response.headersContentLength(),
    DownloadStatus.infoProcessed
  )
}