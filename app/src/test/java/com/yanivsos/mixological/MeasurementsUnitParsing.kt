package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.DrinkUnitMeasurementParser
import com.yanivsos.mixological.conversions.MeasurementUnit
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
        val parser = DrinkUnitMeasurementParser(MeasurementUnit.Oz)
        assert(parser.parseUnit("1/2oz") == null)
        assert(parser.parseUnit("1/3 oz") == MeasurementUnit.Oz)
        assert(parser.parseUnit("11 oZ") == MeasurementUnit.Oz)
        assert(parser.parseUnit("32 OZ") == MeasurementUnit.Oz)
        assert(parser.parseUnit("32 Oz") == MeasurementUnit.Oz)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 oz white") == MeasurementUnit.Oz)
    }

    @Test
    fun testCl() {
        val parser = DrinkUnitMeasurementParser(MeasurementUnit.Cl)
        assert(parser.parseUnit("1/2cl") == null)
        assert(parser.parseUnit("1/3 cl") == MeasurementUnit.Cl)
        assert(parser.parseUnit("4.5 CL") == MeasurementUnit.Cl)
        assert(parser.parseUnit("32 Cl") == MeasurementUnit.Cl)
        assert(parser.parseUnit("32 cL") == MeasurementUnit.Cl)
        assert(parser.parseUnit("32 Ozia") == null)
        assert(parser.parseUnit("1/2 cl white") == MeasurementUnit.Cl)
    }

    @Test
    fun testMl() {
        val drinkUnit = MeasurementUnit.Ml
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
