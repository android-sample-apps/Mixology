package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.DrinkUnitMeasurementParser
import com.yanivsos.mixological.conversions.MeasurementQuantityParser
import com.yanivsos.mixological.conversions.MeasurementUnit
import org.junit.Test

class FullConversions {

    @Test
    fun testUnitReplacement() {
        val parser = DrinkUnitMeasurementParser(MeasurementUnit.Oz)
        assert(parser.replaceMeasurement("1 1/2 oz", MeasurementUnit.Ml) == "1 1/2 ml")
        assert(parser.replaceMeasurement("1 oz", MeasurementUnit.Ml) == "1 ml")
        assert(parser.replaceMeasurement("1 1/2 oz white", MeasurementUnit.Ml) == "1 1/2 ml white")
    }

    @Test
    fun testOzToMlConversion() {
        val parser = MeasurementQuantityParser()
        assert(parser.parseTo("1 1/2 oz", MeasurementUnit.Ml) == "45 ml")
        assert(parser.parseTo("1/2 oz", MeasurementUnit.Ml) == "15 ml")
        assert(parser.parseTo("1 oz", MeasurementUnit.Ml) == "30 ml")
        assert(parser.parseTo("1 - 2 oz", MeasurementUnit.Ml) == "30 - 60 ml")
        assert(parser.parseTo("1 1/2 oz white", MeasurementUnit.Ml) == "45 ml white")
    }

    @Test
    fun testMlToOzConversion() {
        val parser = MeasurementQuantityParser()
        assert(parser.parseTo("45 ml", MeasurementUnit.Oz) == "1.5 oz")
        assert(parser.parseTo("15 ml", MeasurementUnit.Oz) == "0.5 oz")
        assert(parser.parseTo("30 ml", MeasurementUnit.Oz) == "1 oz")
        assert(parser.parseTo("15 - 30 ml", MeasurementUnit.Oz) == "0.5 - 1 oz")
        assert(parser.parseTo("75 ml", MeasurementUnit.Oz) == "2.5 oz")
    }

    @Test
    fun testClToMlConversion() {
        val parser = MeasurementQuantityParser()
        assert(parser.parseTo("45 cl", MeasurementUnit.Ml) == "450 ml")
        assert(parser.parseTo("30 cl", MeasurementUnit.Ml) == "300 ml")
        assert(parser.parseTo("4.5 cl", MeasurementUnit.Ml) == "45 ml")
        assert(parser.parseTo("1.5 - 4.5 cl", MeasurementUnit.Ml) == "15 - 45 ml")
        assert(parser.parseTo("7.2 cl", MeasurementUnit.Ml) == "72 ml")
    }

    @Test
    fun  testDoubleRounding() {
        val parser = MeasurementQuantityParser()
        assert(parser.parseTo("1 /3 oz", MeasurementUnit.Ml) == "10 ml")
        assert(parser.parseTo("0.3 oz", MeasurementUnit.Ml) == "9 ml")
        assert(parser.parseTo("4.543876 cl", MeasurementUnit.Ml) == "45.4 ml")
        assert(parser.parseTo("0.4543876 cl", MeasurementUnit.Ml) == "4.5 ml")
        assert(parser.parseTo("4.573876 cl", MeasurementUnit.Ml) == "45.7 ml")
        assert(parser.parseTo("4.579876 cl", MeasurementUnit.Ml) == "45.8 ml")
    }
}

