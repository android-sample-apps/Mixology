package com.yanivsos.conversions

import com.yanivsos.conversions.units.FluidUnitParser
import com.yanivsos.conversions.units.FluidUnits
import org.junit.Test

/*
*   1 2/3 oz
    1/3 oz
    1 - 2 oz
    1/3 - 1/4 oz
    1/2 oz white
* */
class FluidUnitParsing {

    @Test
    fun testOz() {
        val parser = FluidUnitParser(FluidUnits.Oz)
        assert(parser.parseUnit("1/2oz") == null)
        assert(parser.parseUnit("1/3 oz") == FluidUnits.Oz)
        assert(parser.parseUnit("11 oZ") == FluidUnits.Oz)
        assert(parser.parseUnit("32 OZ") == FluidUnits.Oz)
        assert(parser.parseUnit("32 Oz") == FluidUnits.Oz)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 oz white") == FluidUnits.Oz)
    }

    @Test
    fun testCl() {
        val parser = FluidUnitParser(FluidUnits.Cl)
        assert(parser.parseUnit("1/2cl") == null)
        assert(parser.parseUnit("1/3 cl") == FluidUnits.Cl)
        assert(parser.parseUnit("4.5 CL") == FluidUnits.Cl)
        assert(parser.parseUnit("32 Cl") == FluidUnits.Cl)
        assert(parser.parseUnit("32 cL") == FluidUnits.Cl)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 cl white") == FluidUnits.Cl)
    }

    @Test
    fun testMl() {
        val drinkUnit = FluidUnits.Ml
        val parser = FluidUnitParser(drinkUnit)
        assert(parser.parseUnit("1/2ml") == null)
        assert(parser.parseUnit("1/3 ml") == drinkUnit)
        assert(parser.parseUnit("4.5 Ml") == drinkUnit)
        assert(parser.parseUnit("32 Ml") == drinkUnit)
        assert(parser.parseUnit("32 ML") == drinkUnit)
        assert(parser.parseUnit("32 mlas") == null)
        assert(parser.parseUnit("1/2 ml white") == drinkUnit)
    }
}
