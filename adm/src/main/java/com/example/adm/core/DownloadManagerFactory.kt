package com.example.adm.core

import com.example.adm.http.HTTPDownloadManager
import com.example.adm.http.HTTPDownloadManagerFactory

abstract class DownloadManagerFactory{

  abstract fun makeDownloadManager(): DownloadManager

  companion object {
    inline fun <reified T : DownloadManager> createFactory(): DownloadManagerFactory =
      when (T::class) {
        HTTPDownloadManager::class -> HTTPDownloadManagerFactory()
        else -> throw IllegalArgumentException()
      }
  }
}