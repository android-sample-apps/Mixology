package com.yanivsos.conversions.units

import com.yanivsos.conversions.units.system.FluidSystemConverter
import com.yanivsos.conversions.units.system.MeasurementSystem

class FluidUnitsToSystemConverter(
    private val system: MeasurementSystem
) {
    private val fluidUnitsConverter = FluidUnitsConverter()
    private val fluidSystemConverter = FluidSystemConverter()

    fun parseTo(measurement: String): String {
        return fluidUnitsConverter.parseTo(measurement) { unitOfSystem(it) }
    }

    private fun unitOfSystem(unit: FluidUnits): FluidUnits {
        return when (system) {
            MeasurementSystem.Metric -> fluidSystemConverter.toMetricUnit(unit)
            MeasurementSystem.Imperial -> fluidSystemConverter.toImperialUnit(unit)
        }
    }
}
