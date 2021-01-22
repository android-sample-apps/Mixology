package com.yanivsos.mixological.conversions

import timber.log.Timber

class MeasurementSystemConverter(
    private val system: MeasurementSystem = MeasurementSystem.Metric,
) {
    private val fractionNumberParser = FractionNumberParser()
    private val decimalParser = DecimalParser()

    fun convert(measurement: String): String {
        val srcDrinkUnit = measurement.parseDrinkUnit() ?: return measurement

        val convertor:  (Double) -> String = {  srcValue ->
            srcDrinkUnit.toMetric(srcValue).value.also {
                Timber.d("converted $srcValue from $srcDrinkUnit to $it")
            }
        }

        return when {
            decimalParser.containsMatch(measurement) -> decimalParser.convert(measurement, convertor)
            fractionNumberParser.containsMatch(measurement)  -> fractionNumberParser.convert(measurement, convertor)
            else  ->  measurement
        }

    }
}


fun MeasurementUnit.toMetricUnit():  MeasurementUnit {
    return when(this) {
        MeasurementUnit.Oz -> MeasurementUnit.Ml
        MeasurementUnit.Cl -> MeasurementUnit.Ml
        MeasurementUnit.Ml -> MeasurementUnit.Ml
        MeasurementUnit.Quart -> MeasurementUnit.Ml
        MeasurementUnit.Quart -> MeasurementUnit.Ml
        MeasurementUnit.Liter -> MeasurementUnit.Liter
        MeasurementUnit.Gallon -> MeasurementUnit.Liter
        MeasurementUnit.Pint -> MeasurementUnit.Ml
    }
}

fun MeasurementUnit.toImperialUnit():  MeasurementUnit {
    return when(this) {
        MeasurementUnit.Oz -> MeasurementUnit.Oz
        MeasurementUnit.Cl -> MeasurementUnit.Oz
        MeasurementUnit.Ml -> MeasurementUnit.Oz
        MeasurementUnit.Quart -> MeasurementUnit.Quart
        MeasurementUnit.Quart -> MeasurementUnit.Quart
        MeasurementUnit.Liter -> MeasurementUnit.Pint
        MeasurementUnit.Gallon -> MeasurementUnit.Gallon
        MeasurementUnit.Pint -> MeasurementUnit.Pint
    }
}
