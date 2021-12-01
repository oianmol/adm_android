package com.example.adm.core

import com.example.adm.events.DownloadStatus

abstract class DownloadItem {
  abstract fun url(): String
  abstract fun type(): String
  abstract fun size(): Long
  abstract fun state(): DownloadStatus
}
