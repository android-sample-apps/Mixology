package com.yanivsos.mixological.ui

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.droidnet.DroidNet
import com.yanivsos.mixological.BuildConfig
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.v2.startup.FetchPreviewsWorker
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("unused")
class CocktailApplication : Application() {

    private val lifecycleScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineName("CocktailApplication"))

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }
        super.onCreate()
        observeDarkMode()
        fetchPreviews()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    private fun observeDarkMode() {
        AppSettings
            .darkModeEnabledFlow
            .onEach { isDarkMode -> setNightMode(isDarkMode) }
            .launchIn(lifecycleScope)
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
}


fun setNightMode(isNightMode: Boolean) {
    val mode =
        if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    AppCompatDelegate.setDefaultNightMode(mode)
}
