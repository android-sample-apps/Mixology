package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Milliliter {

    @Test
    fun toOz() {
        assert(FluidUnits.Milliliter.toOz(1.0) == 1.0 / 30.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Milliliter.toCl(1.0) == 0.1)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Milliliter.toMl(1.0) == 1.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Milliliter.toQuart(30.0) == 0.03125)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Milliliter.toLiter(1000.0) == 1.0)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Milliliter.toGallon(30.0) == 0.0078125)
    }
}
