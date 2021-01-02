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

    fun replaceMeasurement(measurement: String, dstDrinkUnit: DrinkUnit): String {
        val nameRegex  = "\\b${drinkUnit.name}\\b".toRegex()
        return nameRegex.replace(measurement, dstDrinkUnit.name)
    }

}

private val drinkUnits = listOf(DrinkUnit.Ml, DrinkUnit.Cl, DrinkUnit.Oz)

fun String.parseDrinkUnit(): DrinkUnit? {
    drinkUnits.forEach { drinkUnit ->
        if (DrinkUnitMeasurementParser(drinkUnit).parseUnit(this) != null) return drinkUnit
    }
    return null
}
