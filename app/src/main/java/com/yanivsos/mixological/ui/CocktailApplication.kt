package com.yanivsos.mixological.ui

import android.app.Application
import com.droidnet.DroidNet
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase

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
        // TODO: 05/04/2021 refactor this - convert to app initializer?
//        val fetchAllPreviews: FetchAllPreviewsUseCase by inject()
//        fetchAllPreviews.fetchAll()
    }

    private fun setNightMode() {
        SetNightModeUseCase().setNightMode(AppSettings.darkModeEnabled)
    }
}
