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
        MeasurementUnit.Qt -> MeasurementUnit.Ml
        MeasurementUnit.Quart -> MeasurementUnit.Ml
        MeasurementUnit.L -> MeasurementUnit.L
        MeasurementUnit.Gal -> MeasurementUnit.L
        MeasurementUnit.Pint -> MeasurementUnit.Ml
    }
}

fun MeasurementUnit.toImperialUnit():  MeasurementUnit {
    return when(this) {
        MeasurementUnit.Oz -> MeasurementUnit.Oz
        MeasurementUnit.Cl -> MeasurementUnit.Oz
        MeasurementUnit.Ml -> MeasurementUnit.Oz
        MeasurementUnit.Qt -> MeasurementUnit.Qt
        MeasurementUnit.Quart -> MeasurementUnit.Quart
        MeasurementUnit.L -> MeasurementUnit.Pint
        MeasurementUnit.Gal -> MeasurementUnit.Gal
        MeasurementUnit.Pint -> MeasurementUnit.Pint
    }
}
