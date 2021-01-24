package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.ViewModel
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase

class SettingsViewModel(
    private val setNightModeUseCase: SetNightModeUseCase
) : ViewModel() {

    var darkModeEnabled: Boolean = AppSettings.darkModeEnabled
        get() = AppSettings.darkModeEnabled
        set(value) {
            toggleDarkMode(value)
            field = value
        }



    private fun toggleDarkMode(darkModeEnabled: Boolean) {
        AppSettings.darkModeEnabled = darkModeEnabled
        setNightModeUseCase.setNightMode(darkModeEnabled)
    }
}
