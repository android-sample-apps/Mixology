package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.ui.models.AppSettings
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    fun toggleDarkMode(darkModeEnabled: Boolean) {
        viewModelScope.launch {
            AppSettings.setDarkModeEnabled(darkModeEnabled)
        }
    }
}
