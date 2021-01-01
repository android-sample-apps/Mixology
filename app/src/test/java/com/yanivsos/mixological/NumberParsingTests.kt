package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.MeasurementNumberParsing
import com.yanivsos.mixological.conversions.parseFraction
import org.junit.Test

/*
* 1/2 = 0.5
* 1 / 2 = 0.5
* 122 / 9 = = 13.55555556
* 1 1 / 2 = 1.5
* 1 1/2 = 1.5
*
* 1 - 3 = 1 - 3
* 1 1/2 - 3  2/3 =
* */
class NumberParsingTests {

    @Test
    fun testFractionRegex() {
        val fractionRegex = MeasurementNumberParsing.fractionRegex
        assert("1/2".matches(fractionRegex))
        assert("1/ 2".matches(fractionRegex))
        assert("1 / 2".matches(fractionRegex))
        assert("1 /2".matches(fractionRegex))
        assert("1 /  2".matches(fractionRegex))
        assert("1 /  2".matches(fractionRegex))
        assert("1 1 /  2".matches(fractionRegex).not())

    }

    @Test
    fun testFraction() {
        assert("1/2".parseFraction() == 0.5)
        assert("1 /2".parseFraction() == 0.5)
        assert("1 / 2".parseFraction() == 0.5)
        assert("1 / 3".parseFraction() == 1.0.div(3))
    }

    @Test
    fun splitNumbers() {
        val parser = MeasurementNumberParsing()
        val parseToNumber: (String) -> CharSequence = { "($it)" }
        assert(parser.parseMeasurements("1 1/2 - 2 1/3 oz white", parseToNumber) == "(1 1/2) - (2 1/3) oz white")
        assert(parser.parseMeasurements("1/2 oz", parseToNumber) == "(1/2) oz")
        assert(parser.parseMeasurements("1-2 oz", parseToNumber) == "(1)-(2) oz")
        assert(parser.parseMeasurements("1 oz", parseToNumber) == "(1) oz")
    }
}
