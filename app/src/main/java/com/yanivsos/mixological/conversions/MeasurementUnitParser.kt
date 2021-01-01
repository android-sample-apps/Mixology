package com.yanivsos.mixological.conversions

interface MeasurementParser {
    fun parseUnit(measurement: String): DrinkUnit?
}

private fun measurementRegex(unit: String): Regex {
    return Regex(".*\\b(?i)$unit\\b.*")
}

class DrinkUnitMeasurementParser(
    private val drinkUnit: DrinkUnit
) : MeasurementParser {

    private val regex = measurementRegex(drinkUnit.name)

    override fun parseUnit(measurement: String): DrinkUnit? {
        return if (measurement.contains(regex)) {
            drinkUnit
        } else {
            null
        }
    }
}
