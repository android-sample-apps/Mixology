package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.conversions.units.system.MeasurementSystem
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_IMPERIAL
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_METRIC
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_ORIGINAL
import com.yanivsos.mixological.conversions.MeasurementPreference
import com.yanivsos.mixological.ui.models.AppSettings
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel : ViewModel() {

    fun changeMeasurementSystemPreference(systemPreference: MeasurementPreference) {
        Timber.d("changeMeasurementSystemPreference: $systemPreference")
        AppSettings.measurementSystem = when (systemPreference) {
            MeasurementPreference.Original -> MEASUREMENT_SYSTEM_ORIGINAL
            is MeasurementPreference.System -> mapMeasurementSystemPreference(systemPreference)
        }
    }

    private fun mapMeasurementSystemPreference(systemPreference: MeasurementPreference.System): Int {
        return when (systemPreference.measurementSystem) {
            MeasurementSystem.Metric -> MEASUREMENT_SYSTEM_METRIC
            MeasurementSystem.Imperial -> MEASUREMENT_SYSTEM_IMPERIAL
        }
    }

    fun toggleDarkMode(darkModeEnabled: Boolean) {
        viewModelScope.launch {
            AppSettings.setDarkModeEnabled(darkModeEnabled)
        }
    }
}
