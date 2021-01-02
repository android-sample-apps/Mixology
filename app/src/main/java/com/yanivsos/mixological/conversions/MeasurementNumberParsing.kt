package com.yanivsos.mixological.conversions

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

/*
*
*   1 2/3 oz
    1/3 oz
    1 - 2 oz
    1/3 - 1/4 oz
    1/2 oz white
*
* */
class MeasurementNumberParsing {

    companion object {
        private const val fractionOnly: String = "((\\d+) */ *(\\d+))"
        private const val numberAndFraction: String =
            "(\\d++(?! */))? *-? *(?:(\\d+) */ *(\\d+))|(\\d+)"
        private const val decimalNumbers = "[0-9]+[.][0-9]*"
        val fractionRegex = Regex(fractionOnly)
        val decimalRegex = Regex(decimalNumbers)
        val numbersAndFractionRegex = Regex(numberAndFraction)
    }

    fun convertMeasurements(measurement: String, parseToNumber: (String) -> CharSequence): String {
        return measurement.parse(numbersAndFractionRegex, parseToNumber).also { println(it) }
    }

    private fun String.parse(regex: Regex, parseToNumber: (String) -> CharSequence): String {
        return regex.replace(this) { parseToNumber(it.groupValues[0]) }
    }

    fun convert(measurement: String, parseToNumber: (Double, DrinkUnit) -> Double): String {
        val drinkUnit = measurement.parseDrinkUnit() ?: return measurement
        if (decimalRegex.matches(measurement)) {
            return decimalRegex.replace(measurement) {
                val value = parseDecimal(it.groupValues[0])
                parseToNumber(value, drinkUnit).prettyDouble()
            }

        }

        return numbersAndFractionRegex.replace(measurement) {
            val value = parseExpression(it.groupValues[0])
            parseToNumber(value, drinkUnit).prettyDouble()
        }
    }

    fun parseTo(measurement: String, dstDrinkUnit: DrinkUnit): String {
        val drinkUnit = measurement.parseDrinkUnit() ?: return measurement
        val measurementParser = DrinkUnitMeasurementParser(drinkUnit)
        val replacedMeasurement = measurementParser.replaceMeasurement(measurement, dstDrinkUnit)
        if (decimalRegex.containsMatchIn(measurement)) {
            return decimalRegex.replace(replacedMeasurement) {
                val value = parseDecimal(it.groupValues[0])
                drinkUnit.convertTo(dstDrinkUnit, value).prettyDouble()
            }

        }
        return numbersAndFractionRegex.replace(replacedMeasurement) {
            val value = parseExpression(it.groupValues[0])
            drinkUnit.convertTo(dstDrinkUnit, value).prettyDouble()
        }
    }

    fun convert(measurement: String, drinkUnit: DrinkUnit): String {
        return convert(measurement) { value, originalDrinkUnit ->
            originalDrinkUnit.convertTo(drinkUnit, value)
        }
    }

    fun parseExpression(expression: String): Double {
        //1 1/2
        return expression.parse(fractionRegex) { fraction ->
            fraction.parseFraction().toString()
        }
            .replace("\\s+".toRegex(), " ")
            .split(" ")
            .sumByDouble {
                it.toDouble()
            }
    }

    fun parseDecimal(expression: String): Double {
        //1 1/2
        return expression.toDouble()
    }

}


fun String.parseFraction(): Double {
    val split = trim().split("/")
    return split[0].toDouble().div(split[1].toDouble())
}

fun Double.prettyDouble(): String {
    return this.let { double ->
        if (double == floor(double)) {
            double.toInt().toString()
        } else {
            double.roundOffDecimal().toString()
        }
    }
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_DOWN
    return df.format(this).toDouble()
}
