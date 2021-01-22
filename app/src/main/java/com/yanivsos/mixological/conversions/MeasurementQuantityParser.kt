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
interface NumberParser {
    fun containsMatch(input: String): Boolean
    fun parse(input: String, src: MeasurementUnit, dst: MeasurementUnit): String
    fun MeasurementUnit.measurementConversion(dst: MeasurementUnit, value: Double): String {
        return this.convertTo(value, dst).prettyDouble()
    }
}

class DecimalParser : NumberParser {
    companion object {
        private const val decimalNumbers = "[0-9]+[.][0-9]*"
        val decimalRegex = Regex(decimalNumbers)
    }

    override fun containsMatch(input: String): Boolean {
        return decimalRegex.containsMatchIn(input)
    }

    override fun parse(
        input: String, src: MeasurementUnit, dst: MeasurementUnit
    ): String {
        return decimalRegex.replace(input) {
            val value = parseDecimal(it.groupValues[0])
            src.measurementConversion(dst, value)
        }
    }

    private fun parseDecimal(expression: String): Double {
        return expression.toDouble()
    }
}

class FractionNumberParser : NumberParser {

    companion object {
        private const val numberAndFraction: String =
            "(\\d++(?! */))? *-? *(?:(\\d+) */ *(\\d+))|(\\d+)"
        private const val fractionOnly: String = "((\\d+) */ *(\\d+))"
        val numbersAndFractionRegex = Regex(numberAndFraction)
        val fractionRegex = Regex(fractionOnly)
    }

    override fun containsMatch(input: String): Boolean {
        return numbersAndFractionRegex.containsMatchIn(input)
    }

    override fun parse(input: String, src: MeasurementUnit, dst: MeasurementUnit): String {
        return numbersAndFractionRegex.replace(input) {
            val value = parseNumbersAndFractionsExpression(it.groupValues[0])
            src.measurementConversion(dst, value)
        }
    }

    private fun parseNumbersAndFractionsExpression(expression: String): Double {
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

    private fun String.parse(regex: Regex, parseToNumber: (String) -> CharSequence): String {
        return regex.replace(this) { parseToNumber(it.groupValues[0]) }
    }
}

class MeasurementQuantityParser {

    private val fractionNumberParser = FractionNumberParser()
    private val decimalParser = DecimalParser()

    fun parseTo(measurement: String, dstMeasurementUnit: MeasurementUnit): String {
        val srcDrinkUnit = measurement.parseDrinkUnit() ?: return measurement

        val measurementParser = DrinkUnitMeasurementParser(srcDrinkUnit)
        val replacedMeasurement =
            measurementParser.replaceMeasurement(measurement, dstMeasurementUnit)

        return if (decimalParser.containsMatch(replacedMeasurement)) {
            decimalParser.parse(replacedMeasurement, srcDrinkUnit, dstMeasurementUnit)

        } else {
            fractionNumberParser.parse(replacedMeasurement, srcDrinkUnit, dstMeasurementUnit)
        }
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
