package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.DrinkUnit
import com.yanivsos.mixological.conversions.DrinkUnitMeasurementParser
import org.junit.Test

/*
*   1 2/3 oz
    1/3 oz
    1 - 2 oz
    1/3 - 1/4 oz
    1/2 oz white
* */
class MeasurementsUnitParsing {

    @Test
    fun testOz() {
        val parser = DrinkUnitMeasurementParser(DrinkUnit.Oz)
        assert(parser.parseUnit("1/2oz") == null)
        assert(parser.parseUnit("1/3 oz") == DrinkUnit.Oz)
        assert(parser.parseUnit("11 oZ") == DrinkUnit.Oz)
        assert(parser.parseUnit("32 OZ") == DrinkUnit.Oz)
        assert(parser.parseUnit("32 Oz") == DrinkUnit.Oz)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 oz white") == DrinkUnit.Oz)
    }

    @Test
    fun testCl() {
        val parser = DrinkUnitMeasurementParser(DrinkUnit.Cl)
        assert(parser.parseUnit("1/2cl") == null)
        assert(parser.parseUnit("1/3 cl") == DrinkUnit.Cl)
        assert(parser.parseUnit("4.5 CL") == DrinkUnit.Cl)
        assert(parser.parseUnit("32 Cl") == DrinkUnit.Cl)
        assert(parser.parseUnit("32 cL") == DrinkUnit.Cl)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 cl white") == DrinkUnit.Cl)
    }

    @Test
    fun testMl() {
        val drinkUnit = DrinkUnit.Ml
        val parser = DrinkUnitMeasurementParser(drinkUnit)
        assert(parser.parseUnit("1/2ml") == null)
        assert(parser.parseUnit("1/3 ml") == drinkUnit)
        assert(parser.parseUnit("4.5 Ml") == drinkUnit)
        assert(parser.parseUnit("32 Ml") == drinkUnit)
        assert(parser.parseUnit("32 ML") == drinkUnit)
        assert(parser.parseUnit("32 mlas") == null)
        assert(parser.parseUnit("1/2 ml white") == drinkUnit)
    }
}
