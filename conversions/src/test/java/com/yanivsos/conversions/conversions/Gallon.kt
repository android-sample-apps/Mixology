package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Gallon {

    @Test
    fun toOz() {
        assert(FluidUnits.Gallon.toOz(1.0) == 128.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Gallon.toCl(1.0) == 384.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Gallon.toMl(1.0) == 3840.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Gallon.toQuart(1.0) == 4.0)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Gallon.toLiter(1.0) == 3.78543)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Gallon.toGallon(2.0) == 2.0)
    }
}
