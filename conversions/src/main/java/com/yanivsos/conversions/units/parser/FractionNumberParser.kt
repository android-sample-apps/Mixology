package com.yanivsos.conversions.units.parser

import com.yanivsos.conversions.units.MeasureUnit

class FractionNumberParser<T : MeasureUnit>(
    override val numberConverter: NumberConverter<T>
) : NumberParser<T> {

    companion object {
        private const val numberAndFraction: String =
            "(\\d++(?! */))? *-? *(?:(\\d+) */ *(\\d+))|(\\d+)"
        private const val fractionOnly: String = "((\\d+) */ *(\\d+))"
        val numbersAndFractionRegex = Regex(numberAndFraction)
        val fractionRegex = Regex(fractionOnly)

        fun parseFraction(input: String): Double {
            val split = input.trim().split("/")
            return split[0].toDouble().div(split[1].toDouble())
        }
    }

    override fun containsMatch(input: String): Boolean {
        return numbersAndFractionRegex.containsMatchIn(input)
    }

    override fun convertMeasurement(input: String, src: T, dst: T): String {
        return numbersAndFractionRegex.replace(input) {
            val value = parseNumbersAndFractionsExpression(it.groupValues[0])
            val convertedValue = numberConverter.convertTo(src, dst, value)
            numberConverter.valueToString(convertedValue)
        }
    }

    private fun parseNumbersAndFractionsExpression(expression: String): Double {
        //1 1/2
        return expression.parse(fractionRegex) { fraction ->
            parseFraction(fraction).toString()
        }
            .replace("\\s+".toRegex(), " ")
            .split(" ")
            .sumOf {
                it.toDouble()
            }
    }

    private fun String.parse(regex: Regex, parseToNumber: (String) -> CharSequence): String {
        return regex.replace(this) { parseToNumber(it.groupValues[0]) }
    }

}
