package com.example.adm.http

import com.example.adm.core.DownloadItem
import com.example.adm.events.DownloadStatus
import okhttp3.Response
import okhttp3.internal.headersContentLength

class HTTPDownloadInfo(
  private val url: String,
  private val type: String,
  private var size: Long,
  private var state: DownloadStatus,
) : DownloadItem() {

  override fun url() = url
  override fun type() = type
  override fun size() = size
  override fun state() = state
}