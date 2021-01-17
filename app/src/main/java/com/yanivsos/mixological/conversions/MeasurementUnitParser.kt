package com.yanivsos.mixological.conversions

interface MeasurementParser {
    fun parseUnit(measurement: String): MeasurementUnit?
}

private fun measurementRegex(unit: String): Regex {
    return Regex(".*\\b(?i)$unit\\b.*")
}

class DrinkUnitMeasurementParser(
    private val measurementUnit: MeasurementUnit
) : MeasurementParser {

    private val regex = measurementRegex(measurementUnit.name)

    override fun parseUnit(measurement: String): MeasurementUnit? {
        return if (measurement.contains(regex)) {
            measurementUnit
        } else {
            null
        }
    }

    fun replaceMeasurement(measurement: String, dstMeasurementUnit: MeasurementUnit): String {
        val nameRegex = "\\b(?i)${measurementUnit.name}\\b".toRegex()
        return nameRegex.replace(measurement, dstMeasurementUnit.name)
    }

}

private val drinkUnits = listOf(
    MeasurementUnit.Ml,
    MeasurementUnit.Cl,
    MeasurementUnit.Oz,
    MeasurementUnit.Gal,
    MeasurementUnit.L,
    MeasurementUnit.Pint,
    MeasurementUnit.Qt,
    MeasurementUnit.Quart
)

fun String.parseDrinkUnit(): MeasurementUnit? {
    drinkUnits.forEach { drinkUnit ->
        if (DrinkUnitMeasurementParser(drinkUnit).parseUnit(this) != null) return drinkUnit
    }
    return null
}
