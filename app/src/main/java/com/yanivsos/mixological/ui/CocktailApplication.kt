package com.yanivsos.mixological.ui

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.droidnet.DroidNet
import com.yanivsos.mixological.di.KoinStarter
import com.yanivsos.mixological.domain.FetchAllPreviewsUseCase
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase
import org.koin.android.ext.android.inject
import timber.log.Timber

@Suppress("unused")
class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        startKoin()
        startTimber()
        startKotpref()
        startDroidNet()
        setNightMode()
        fetchPreviews()
    }

    private fun startKotpref() {
        Kotpref.init(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    private fun startDroidNet() {
        DroidNet.init(this)
    }

    private fun fetchPreviews() {
        val fetchAllPreviews: FetchAllPreviewsUseCase by inject()
        fetchAllPreviews.fetchAll()
    }

    private fun startTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun startKoin() {
        koinStarter.start(this)
    }

    private fun setNightMode() {
        SetNightModeUseCase().setNightMode(AppSettings.darkModeEnabled)
    }
}