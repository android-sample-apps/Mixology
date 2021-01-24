package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Pint {

    @Test
    fun toOz() {
        assert(FluidUnits.Pint.toOz(1.0) == 16.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Pint.toCl(1.0) == 48.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Pint.toMl(1.0) == 480.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Pint.toQuart(1.0) == 0.5)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Pint.toLiter(1.0) == 0.480)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Pint.toGallon(1.0) == 0.125)
    }
}
