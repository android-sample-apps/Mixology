package com.yanivsos.conversions.units.parser

import com.yanivsos.conversions.units.MeasureUnit

class DecimalParser<T : MeasureUnit>(
    override val numberConverter: NumberConverter<T>
) : NumberParser<T> {
    companion object {
        private const val decimalNumbers = "[0-9]+[.][0-9]*"
        val decimalRegex = Regex(decimalNumbers)
    }

    override fun containsMatch(input: String): Boolean {
        return decimalRegex.containsMatchIn(input)
    }

    override fun convertMeasurement(
        input: String, src: T, dst: T
    ): String {
        return decimalRegex.replace(input) {
            val value = parseDecimal(it.groupValues[0])
            val convertedValue = numberConverter.convertTo(src, dst, value)
            numberConverter.valueToString(convertedValue)
        }
    }

    private fun parseDecimal(expression: String): Double {
        return expression.toDouble()
    }
}
