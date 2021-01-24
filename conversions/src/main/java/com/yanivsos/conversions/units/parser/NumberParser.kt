package com.yanivsos.conversions.units.parser

import com.yanivsos.conversions.units.MeasureUnit

interface NumberParser<T: MeasureUnit> {
    fun containsMatch(input: String): Boolean
    fun convertMeasurement(input: String, src: T, dst: T): String
    val numberConverter: NumberConverter<T>
}

