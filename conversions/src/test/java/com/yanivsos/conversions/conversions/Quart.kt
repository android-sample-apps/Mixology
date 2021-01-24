package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Quart {

    @Test
    fun toOz() {
        assert(FluidUnits.Quart.toOz(1.0) == 32.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Quart.toCl(1.0) == 96.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Quart.toMl(1.0) == 960.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Quart.toQuart(3.0) == 3.0)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Quart.toLiter(1.0) == 0.96)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Quart.toGallon(1.0) == 0.25)
    }
}
