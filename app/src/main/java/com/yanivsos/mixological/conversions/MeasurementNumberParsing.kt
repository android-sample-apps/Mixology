package com.yanivsos.mixological.conversions

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
        private const val numberAndFraction: String = "(\\d++(?! */))? *-? *(?:(\\d+) */ *(\\d+))|(\\d+)"
        val fractionRegex = Regex(fractionOnly)
        val numbersAndFractionRegex = Regex(numberAndFraction)
    }

    fun parseMeasurements(measurement: String, parseToNumber: (String) -> CharSequence): String {
        return measurement.parse(numbersAndFractionRegex, parseToNumber).also { println(it) }
    }

    private fun String.parse(regex: Regex, parseToNumber: (String) -> CharSequence): String {
        return regex.replace(this) { parseToNumber(it.groupValues[0]) }
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
            double.toString()
        }
    }
}


