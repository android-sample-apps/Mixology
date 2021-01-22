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

    private val regexList: List<Regex> = measurementUnit
        .names
        .map { measurementRegex(it) }

    override fun parseUnit(measurement: String): MeasurementUnit? {
        for (regex in regexList) {
            if (measurement.contains(regex)) return measurementUnit
        }

        return null
    }

    fun replaceMeasurement(measurement: String, dstMeasurementUnit: MeasurementUnit): String {
        val nameRegex = "\\b(?i)${measurementUnit.names.first()}\\b".toRegex()
        return nameRegex.replace(measurement, dstMeasurementUnit.names.first())
    }

}

private val drinkUnits = listOf(
    MeasurementUnit.Ml,
    MeasurementUnit.Cl,
    MeasurementUnit.Oz,
    MeasurementUnit.Gallon,
    MeasurementUnit.Liter,
    MeasurementUnit.Pint,
    MeasurementUnit.Quart,
    MeasurementUnit.Quart
)

fun String.parseDrinkUnit(): MeasurementUnit? {
    drinkUnits.forEach { drinkUnit ->
        if (DrinkUnitMeasurementParser(drinkUnit).parseUnit(this) != null) return drinkUnit
    }
    return null
}
