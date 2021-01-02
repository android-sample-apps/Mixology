package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.MeasurementNumberParsing
import org.junit.Test

class FullConversions {

    @Test
    fun testMlToOz() {
        val parser = MeasurementNumberParsing()
        val expression = "1/2 oz"
        val converted = parser.convert(expression) { value, drinkUnit ->
            value * -1
        }

        assert(converted == "-0.5 oz")
    }
}

