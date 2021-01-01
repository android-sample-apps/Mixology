package com.yanivsos.mixological.conversions

import java.lang.Exception

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
        val fractionOnly: String = "((\\d+) */ *(\\d+))"
        val numberAndFraction: String = "(\\d++(?! */))? *-? *(?:(\\d+) */ *(\\d+))|(\\d+)"
        val fractionRegex = Regex(fractionOnly)
        val numbersAndFractionRegex = Regex(numberAndFraction)
    }

    fun parseMeasurements(measurement: String, parseToNumber: (String) -> CharSequence): String {
        return numbersAndFractionRegex.replace(measurement) { matchResult ->
            parseToNumber(matchResult.groupValues[0])
        }
    }


//    fun p
}



fun String.parseFraction(): Double? {
    return try {
        val split = trim().split("/")
        split[0].toDouble().div(split[1].toDouble())

    } catch (e: Exception) {
        null
    }
}


