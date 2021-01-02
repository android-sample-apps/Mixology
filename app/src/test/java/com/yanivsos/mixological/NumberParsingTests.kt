package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.FractionNumberParser
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
        val fractionRegex = FractionNumberParser.fractionRegex
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
}
