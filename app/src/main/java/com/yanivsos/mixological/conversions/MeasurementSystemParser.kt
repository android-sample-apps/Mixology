package com.yanivsos.mixological.conversions

import com.yanivsos.conversions.units.system.MeasurementSystem

const val MEASUREMENT_SYSTEM_ORIGINAL = 0
const val MEASUREMENT_SYSTEM_IMPERIAL = 1
const val MEASUREMENT_SYSTEM_METRIC = 2

object MeasurementSystemParser {

    fun parse(value: Int): MeasurementPreference {
        return when (value) {
            MEASUREMENT_SYSTEM_ORIGINAL -> MeasurementPreference.Original
            MEASUREMENT_SYSTEM_IMPERIAL -> MeasurementPreference.System(MeasurementSystem.Imperial)
            MEASUREMENT_SYSTEM_METRIC -> MeasurementPreference.System(MeasurementSystem.Metric)
            else -> MeasurementPreference.Original
        }
    }
}
