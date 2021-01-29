package com.yanivsos.mixological.conversions

import com.yanivsos.conversions.units.system.MeasurementSystem

object MeasurementSystemParser {

    const val ORIGINAL = 0
    const val IMPERIAL = 1
    const val METRIC = 2

    fun parse(value: Int): MeasurementPreference {
        return when (value) {
            ORIGINAL -> MeasurementPreference.Original
            IMPERIAL -> MeasurementPreference.System(MeasurementSystem.Imperial)
            METRIC -> MeasurementPreference.System(MeasurementSystem.Metric)
            else -> MeasurementPreference.Original
        }
    }
}
