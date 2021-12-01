package com.example.adm.events

import java.lang.Exception

sealed class DownloadStatus {
  object infoProcessed : DownloadStatus()
  object Downloading : DownloadStatus()
  object Paused : DownloadStatus()
  object Cancelled : DownloadStatus()
  class Failed(var exception: Exception) : DownloadStatus()
}