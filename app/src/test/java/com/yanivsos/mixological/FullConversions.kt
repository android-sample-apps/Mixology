package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.DrinkUnit
import com.yanivsos.mixological.conversions.DrinkUnitMeasurementParser
import com.yanivsos.mixological.conversions.MeasurementNumberParsing
import org.junit.Test

class FullConversions {

    @Test
    fun testUnitReplacement() {
        val parser = DrinkUnitMeasurementParser(DrinkUnit.Oz)
        assert(parser.replaceMeasurement("1 1/2 oz", DrinkUnit.Ml) == "1 1/2 ml")
        assert(parser.replaceMeasurement("1 oz", DrinkUnit.Ml) == "1 ml")
        assert(parser.replaceMeasurement("1 1/2 oz white", DrinkUnit.Ml) == "1 1/2 ml white")
    }

    @Test
    fun testOzToMlConversion() {
        val parser = MeasurementNumberParsing()
        assert(parser.parseTo("1 1/2 oz", DrinkUnit.Ml) == "45 ml")
        assert(parser.parseTo("1/2 oz", DrinkUnit.Ml) == "15 ml")
        assert(parser.parseTo("1 oz", DrinkUnit.Ml) == "30 ml")
        assert(parser.parseTo("1 - 2 oz", DrinkUnit.Ml) == "30 - 60 ml")
        assert(parser.parseTo("1 1/2 oz white", DrinkUnit.Ml) == "45 ml white")
    }

    @Test
    fun testMlToOzConversion() {
        val parser = MeasurementNumberParsing()
        assert(parser.parseTo("45 ml", DrinkUnit.Oz) == "1.5 oz")
        assert(parser.parseTo("15 ml", DrinkUnit.Oz) == "0.5 oz")
        assert(parser.parseTo("30 ml", DrinkUnit.Oz) == "1 oz")
        assert(parser.parseTo("15 - 30 ml", DrinkUnit.Oz) == "0.5 - 1 oz")
        assert(parser.parseTo("75 ml", DrinkUnit.Oz) == "2.5 oz")
    }

    @Test
    fun testClToMlConversion() {
        val parser = MeasurementNumberParsing()
        assert(parser.parseTo("45 cl", DrinkUnit.Ml) == "450 ml")
        assert(parser.parseTo("30 cl", DrinkUnit.Ml) == "300 ml")
        assert(parser.parseTo("4.5 cl", DrinkUnit.Ml) == "45 ml")
        assert(parser.parseTo("1.5 - 4.5 cl", DrinkUnit.Ml) == "15 - 45 ml")
        assert(parser.parseTo("7.2 cl", DrinkUnit.Ml) == "72 ml")
    }

    @Test
    fun  testDoubleRounding() {
        val parser = MeasurementNumberParsing()
        assert(parser.parseTo("1 /3 oz", DrinkUnit.Ml) == "10 ml")
        assert(parser.parseTo("0.3 oz", DrinkUnit.Ml) == "9 ml")
        assert(parser.parseTo("4.543876 cl", DrinkUnit.Ml) == "45.4 ml")
        assert(parser.parseTo("0.4543876 cl", DrinkUnit.Ml) == "4.5 ml")
        assert(parser.parseTo("4.573876 cl", DrinkUnit.Ml) == "45.7 ml")
        assert(parser.parseTo("4.579876 cl", DrinkUnit.Ml) == "45.8 ml")
    }
}

