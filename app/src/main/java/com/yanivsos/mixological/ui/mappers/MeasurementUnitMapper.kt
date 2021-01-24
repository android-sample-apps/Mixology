package com.yanivsos.mixological.ui.mappers

import com.yanivsos.conversions.units.FluidUnitsToSystemConverter

interface MeasurementUnitMapper {
    fun mapTo(measurement: String): String
}

class KeepOriginalMeasurementUnitMapper : MeasurementUnitMapper {
    override fun mapTo(measurement: String): String {
        return measurement
    }
}

class FluidMeasurementUnitMapper(
    private val fluidUnitConverter: FluidUnitsToSystemConverter
) : MeasurementUnitMapper {

    override fun mapTo(measurement: String): String {
        return fluidUnitConverter.parseTo(measurement)
    }
}
