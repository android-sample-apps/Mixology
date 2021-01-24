package com.yanivsos.conversions.conversions

import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

class Centiliter {
    
    @Test
    fun toOz() {
        assert(FluidUnits.Centiliter.toOz(3.0) == 1.0)
    }

    @Test
    fun toCl() {
        assert(FluidUnits.Centiliter.toCl(1.0) == 1.0)
    }

    @Test
    fun toMl() {
        assert(FluidUnits.Centiliter.toMl(1.0) == 10.0)
    }

    @Test
    fun toQt() {
        assert(FluidUnits.Centiliter.toQuart(3.0) == 0.03125)
    }

    @Test
    fun toLiter() {
        assert(FluidUnits.Centiliter.toLiter(100.0) == 1.0)
    }

    @Test
    fun toGallon() {
        assert(FluidUnits.Centiliter.toGallon(3.0) == 0.0078125)
    }
}
