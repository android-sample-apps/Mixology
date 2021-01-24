package com.yanivsos.mixological.conversions

import com.yanivsos.conversions.units.system.MeasurementSystem

sealed class MeasurementPreference {
    object Original: MeasurementPreference()
    data class System(val measurementSystem: MeasurementSystem): MeasurementPreference()
}
