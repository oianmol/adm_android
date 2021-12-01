package com.example.adm.core

import com.example.adm.http.DownloadPart
import com.example.adm.http.HTTPDownloadInfo

data class DownloadPromise(
  var uuid: String,
  var url: String,
  var httpDownloadInfo: HTTPDownloadInfo,
  var downloadParts: List<DownloadPart>? = null
)
