package com.example.adm.events

import com.example.adm.core.DownloadPromise

interface DownloadPromiseUpdater {
  fun promiseUpdated(downloadPromise: DownloadPromise)
  fun promiseDeleted(downloadPromise: DownloadPromise)
  fun promiseCreated(downloadPromise: DownloadPromise)
}