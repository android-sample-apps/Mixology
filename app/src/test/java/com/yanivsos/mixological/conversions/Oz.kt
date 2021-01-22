package com.yanivsos.mixological.conversions

import org.junit.Test

class Oz
{
    @Test
    fun toOz() {
        assert(MeasurementUnit.Oz.toOz(1.0) == 1.0)
    }

    @Test
    fun toCl() {
        assert(MeasurementUnit.Oz.toCl(1.0) == 3.0)
    }

    @Test
    fun toMl() {
        assert(MeasurementUnit.Oz.toMl(1.0) == 30.0)
    }

    @Test
    fun toQt() {
        assert(MeasurementUnit.Oz.toQuart(1.0) == 0.03125)
    }

    @Test
    fun toLiter() {
        assert(MeasurementUnit.Oz.toLiter(1.0) == 0.030)
    }

    @Test
    fun toGallon() {
        assert(MeasurementUnit.Oz.toGallon(1.0) == 0.0078125)
    }
}
