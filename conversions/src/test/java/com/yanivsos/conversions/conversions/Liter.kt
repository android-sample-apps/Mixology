package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Liter {
    
    @Test
    fun toOz() {
        assert(FluidUnits.Liter.toOz(1.0) == (1000 / 30.0))
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Liter.toCl(1.0) == 100.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Liter.toMl(1.0) == 1000.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Liter.toQuart(1.0) == (1000 / 30.0 * 0.03125))
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Liter.toLiter(1.0) == 1.0)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Liter.toGallon(2.0) == (2000 / 30.0 * 0.0078125))
    }
}
