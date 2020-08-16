package com.yanivsos.mixological.ui

import android.app.Application
import com.droidnet.DroidNet
import com.yanivsos.mixological.domain.FetchAllPreviewsUseCase
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase
import org.koin.android.ext.android.inject

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
        val fetchAllPreviews: FetchAllPreviewsUseCase by inject()
        fetchAllPreviews.fetchAll()
    }

    private fun setNightMode() {
        SetNightModeUseCase().setNightMode(AppSettings.darkModeEnabled)
    }
}