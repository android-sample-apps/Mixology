package com.yanivsos.mixological.ui

import android.app.Application
import com.droidnet.DroidNet
import com.yanivsos.mixological.di.KoinStarter
import com.yanivsos.mixological.domain.FetchAllPreviewsUseCase
import org.koin.android.ext.android.inject
import timber.log.Timber

@Suppress("unused")
class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        startKoin()
        startTimber()
        startDroidNet()
        fetchPreviews()
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
}