package com.yanivsos.mixological.domain

import com.yanivsos.mixological.conversions.MeasurementPreference
import com.yanivsos.mixological.conversions.MeasurementSystemParser
import com.yanivsos.mixological.ui.models.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMeasurementPreferenceUseCase {

    val measurementPreference: Flow<MeasurementPreference> = flow {
        MeasurementSystemParser.parse(AppSettings.measurementSystem)
    }
}
