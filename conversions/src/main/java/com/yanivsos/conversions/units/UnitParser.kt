package com.yanivsos.conversions.units

interface UnitParser<T> {
    fun parseUnit(measurement: String): T?
    fun replaceMeasurement(measurement: String, dstMeasurementUnit: FluidUnits): String
}

abstract class BaseUnitParser<T : MeasureUnit>(
    private val unit: T
) : UnitParser<T> {

    private val regexList: List<Regex> = unit
        .names
        .map { unitRegex(it) }


    private fun unitRegex(unit: String): Regex {
        return Regex(".*\\b(?i)$unit\\b.*")
    }


    override fun parseUnit(measurement: String): T? {
        regexList.forEach { regex ->
            if (measurement.contains(regex)) return unit
        }

        return null
    }

    override fun replaceMeasurement(measurement: String, dstMeasurementUnit: FluidUnits): String {
        unit.names.forEach { unitName ->
            val nameRegex = "\\b(?i)${unitName}\\b".toRegex()
            if (nameRegex.containsMatchIn(measurement)) {
                return nameRegex.replace(measurement, dstMeasurementUnit.names.first())
            }
        }

        return measurement
    }
}


class FluidUnitParser(
    srcFluidUnit: FluidUnits
) : BaseUnitParser<FluidUnits>(srcFluidUnit)

