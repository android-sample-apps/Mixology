package com.yanivsos.mixological

import com.yanivsos.mixological.conversions.DrinkUnit
import com.yanivsos.mixological.conversions.parseDrinkUnit
import org.junit.Test

class FullConversions {

    @Test
    fun testMlToOz() {
        val expression = "1 oz"

        expression.convertTo(DrinkUnit.Ml)
    }
}

fun String.convertTo(drinkUnit: DrinkUnit) {
    val originalDrinkUnit = parseDrinkUnit()

}

fun convert(expression: String, srcDrinkUnit: DrinkUnit, dstDrinkUnit: DrinkUnit): String {
    TODO()
}
