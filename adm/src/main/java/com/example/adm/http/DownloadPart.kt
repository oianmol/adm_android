package com.example.adm.http

import com.example.adm.core.DownloadItem
import java.io.File

data class DownloadPart(var startRange: Long, var endRange: Long, val file: File)

fun generateParts(item: DownloadItem, strategy: HTTPDownloadStrategy): List<DownloadPart> {
  val parts = mutableListOf<DownloadPart>()
  var start = 0L
  val splitSize = item.size() / strategy.workers
  while (start != item.size()) {
    val part = DownloadPart(start, 0L,)
    if (part.startRange != 0L) {
      part.startRange.plus(splitSize)
    }
    val remainingPart = item.size() - start
    start += if (remainingPart < splitSize) {
      remainingPart
    } else {
      splitSize
    }
    part.endRange = start
    parts.add(part)
  }
  return parts
}