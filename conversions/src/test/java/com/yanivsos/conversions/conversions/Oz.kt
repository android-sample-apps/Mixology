package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Oz
{
    @Test
    fun toOz() {
        assert(FluidUnits.Oz.toOz(1.0) == 1.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Oz.toCl(1.0) == 3.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Oz.toMl(1.0) == 30.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Oz.toQuart(1.0) == 0.03125)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Oz.toLiter(1.0) == 0.030)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Oz.toGallon(1.0) == 0.0078125)
    }
}
