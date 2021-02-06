package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.ViewModel
import com.yanivsos.conversions.units.system.MeasurementSystem
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_IMPERIAL
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_METRIC
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_ORIGINAL
import com.yanivsos.mixological.conversions.MeasurementPreference
import com.yanivsos.mixological.ui.models.AppSettings
import com.yanivsos.mixological.ui.utils.SetNightModeUseCase
import timber.log.Timber

class SettingsViewModel(
    private val setNightModeUseCase: SetNightModeUseCase
) : ViewModel() {

    var darkModeEnabled: Boolean = AppSettings.darkModeEnabled
        get() = AppSettings.darkModeEnabled
        set(value) {
            toggleDarkMode(value)
            field = value
        }

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

    private fun toggleDarkMode(darkModeEnabled: Boolean) {
        AppSettings.darkModeEnabled = darkModeEnabled
        setNightModeUseCase.setNightMode(darkModeEnabled)
    }
}
