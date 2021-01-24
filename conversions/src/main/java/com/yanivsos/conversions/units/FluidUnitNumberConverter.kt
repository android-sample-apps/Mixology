package com.yanivsos.conversions.units

import com.yanivsos.conversions.units.parser.NumberConverter
import com.yanivsos.conversions.units.parser.prettyDouble

class FluidUnitNumberConverter : NumberConverter<FluidUnits> {

    override fun valueToString(value: Double): String {
        return value.prettyDouble()
    }

    override fun convertTo(src: FluidUnits, dst: FluidUnits, value: Double): Double {
        return src.convertTo(value, dst)
    }
}
