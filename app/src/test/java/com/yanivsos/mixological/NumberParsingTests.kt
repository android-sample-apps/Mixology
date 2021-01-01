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
        assert(
            parser.convertMeasurements(
                "1 1/2 - 2 1/3 oz white",
                parseToNumber
            ) == "(1 1/2) - (2 1/3) oz white"
        )
        assert(parser.convertMeasurements("1/2 oz", parseToNumber) == "(1/2) oz")
        assert(parser.convertMeasurements("1-2 oz", parseToNumber) == "(1)-(2) oz")
        assert(parser.convertMeasurements("1 oz", parseToNumber) == "(1) oz")
    }

    @Test
    fun parseExpressions() {
        val parser = MeasurementNumberParsing()
        assert(parser.parseExpression("1   1 / 2") == 1.5)
        assert(parser.parseExpression("1") == 1.0)
        assert(parser.parseExpression("1 / 2") == 0.5)
        assert(parser.parseExpression("22 / 44") == 0.5)
        assert(parser.parseExpression("22 /44") == 0.5)
        assert(parser.parseExpression("1 22 /44") == 1.5)
    }

    /*@Test
    fun parseSplit() {
        val parser = MeasurementNumberParsing()
        val parseToNumber: (String) -> CharSequence = {
            val value = parser.parseExpression(it)
            if (value == floor(value)) {
                value.toInt().toString()
            } else {
                value.toString()
            }
        }
        assert(
            parser.parseMeasurements(
                "1 1/2 - 2 1/3 oz white",
                parseToNumber
            ) == "1.5 - ${2.0 + (1.0.div(3))} oz white"
        )
        assert(parser.parseMeasurements("1/2 oz", parseToNumber) == "0.5 oz")
        assert(parser.parseMeasurements("1 /2 oz", parseToNumber) == "1-2 oz")
        assert(parser.parseMeasurements("1-2 oz", parseToNumber) == "1-2 oz")
        assert(parser.parseMeasurements("1 oz", parseToNumber) == "1 oz")
//        assert(parser.parseMeasurements("1 1/2 oz", parseToNumber) == "1.5 oz")
    }*/
}
