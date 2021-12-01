package com.example.adm.http

import com.example.adm.core.DownloadManager
import com.example.adm.core.DownloadManagerFactory

class HTTPDownloadManagerFactory : DownloadManagerFactory() {
  override fun makeDownloadManager(): DownloadManager {
    return HTTPDownloadManager()
  }
}