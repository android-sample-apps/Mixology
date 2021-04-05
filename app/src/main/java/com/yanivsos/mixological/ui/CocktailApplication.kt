package com.yanivsos.mixological.ui

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.droidnet.DroidNet
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase
import com.yanivsos.mixological.v2.startup.FetchPreviewsWorker

@Suppress("unused")
class CocktailApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setNightMode()
        fetchPreviews()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    private fun fetchPreviews() {
        OneTimeWorkRequestBuilder<FetchPreviewsWorker>()
            .build()
            .also { worker ->
                WorkManager
                    .getInstance(this)
                    .enqueueUniqueWork(
                        FetchPreviewsWorker.WORKER_NAME,
                        ExistingWorkPolicy.KEEP,
                        worker
                    )
            }
    }

    private fun setNightMode() {
        SetNightModeUseCase().setNightMode(AppSettings.darkModeEnabled)
    }
}
