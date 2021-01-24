package com.yanivsos.conversions.units.parser

import com.yanivsos.conversions.units.MeasureUnit
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

interface NumberConverter<T: MeasureUnit> {
    fun convertTo(src: T, dst: T, value: Double): Double
    fun valueToString(value: Double): String
}

fun Double.prettyDouble(): String {
    return this.let { double ->
        val rounded = double.roundOffDecimal()
        if (rounded == floor(rounded)) {
            rounded.toInt().toString()
        } else {
            rounded.toString()
        }
    }
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_DOWN
    return df.format(this).toDouble()
}
