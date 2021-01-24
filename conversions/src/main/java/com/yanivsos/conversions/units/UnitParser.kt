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
        for (regex in regexList) {
            if (measurement.contains(regex)) return unit
        }

        return null
    }

    override fun replaceMeasurement(measurement: String, dstMeasurementUnit: FluidUnits): String {
        val nameRegex = "\\b(?i)${unit.names.first()}\\b".toRegex()
        return nameRegex.replace(measurement, dstMeasurementUnit.names.first())
    }
}


class FluidUnitParser(
    srcFluidUnit: FluidUnits
) : BaseUnitParser<FluidUnits>(srcFluidUnit)

