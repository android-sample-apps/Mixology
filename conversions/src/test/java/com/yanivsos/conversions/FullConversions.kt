package com.yanivsos.conversions

import com.yanivsos.conversions.units.FluidUnitParser
import com.yanivsos.conversions.units.FluidUnits
import com.yanivsos.conversions.units.FluidUnitsConverter
import org.junit.Test

class FullConversions {

    @Test
    fun testUnitReplacement() {
        val parser = FluidUnitParser(FluidUnits.Oz)
        assert(parser.replaceMeasurement("1 1/2 oz", FluidUnits.Milliliter) == "1 1/2 ml")
        assert(parser.replaceMeasurement("1 oz", FluidUnits.Milliliter) == "1 ml")
        assert(parser.replaceMeasurement("1 1/2 oz white", FluidUnits.Milliliter) == "1 1/2 ml white")
    }

    @Test
    fun testOzToMlConversion() {
        val parser = FluidUnitsConverter()
        assert(parser.parseTo("1 1/2 oz") { FluidUnits.Milliliter } == "45 ml")
        assert(parser.parseTo("1/2 oz") { FluidUnits.Milliliter } == "15 ml")
        assert(parser.parseTo("1 oz") { FluidUnits.Milliliter } == "30 ml")
        assert(parser.parseTo("1 - 2 oz") { FluidUnits.Milliliter } == "30 - 60 ml")
        assert(parser.parseTo("1 1/2 oz white") { FluidUnits.Milliliter } == "45 ml white")
    }

    @Test
    fun testMlToOzConversion() {
        val parser = FluidUnitsConverter()
        assert(parser.parseTo("45 ml") { FluidUnits.Oz } == "1.5 oz")
        assert(parser.parseTo("15 ml") { FluidUnits.Oz } == "0.5 oz")
        assert(parser.parseTo("30 ml") { FluidUnits.Oz } == "1 oz")
        assert(parser.parseTo("15 - 30 ml") { FluidUnits.Oz } == "0.5 - 1 oz")
        assert(parser.parseTo("75 ml") { FluidUnits.Oz } == "2.5 oz")
    }

    @Test
    fun testClToMlConversion() {
        val parser = FluidUnitsConverter()
        assert(parser.parseTo("45 cl") { FluidUnits.Milliliter } == "450 ml")
        assert(parser.parseTo("30 cl") { FluidUnits.Milliliter } == "300 ml")
        assert(parser.parseTo("4.5 cl") { FluidUnits.Milliliter } == "45 ml")
        assert(parser.parseTo("1.5 - 4.5 cl") { FluidUnits.Milliliter } == "15 - 45 ml")
        assert(parser.parseTo("7.2 cl") { FluidUnits.Milliliter } == "72 ml")
    }

    @Test
    fun testDoubleRounding() {
        val parser = FluidUnitsConverter()
        assert(parser.parseTo("1 /3 oz") { FluidUnits.Milliliter } == "10 ml")
        assert(parser.parseTo("0.3 oz") { FluidUnits.Milliliter } == "9 ml")
        assert(parser.parseTo("4.543876 cl") { FluidUnits.Milliliter } == "45.4 ml")
        assert(parser.parseTo("0.4543876 cl") { FluidUnits.Milliliter } == "4.5 ml")
        assert(parser.parseTo("4.573876 cl") { FluidUnits.Milliliter } == "45.7 ml")
        assert(parser.parseTo("4.579876 cl") { FluidUnits.Milliliter } == "45.8 ml")
        assert(parser.parseTo("1.039876 cl") { FluidUnits.Centiliter } == "1 cl")
        assert(parser.parseTo("1.2 cl") { FluidUnits.Centiliter } == "1.2 cl")
    }
}

