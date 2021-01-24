package com.yanivsos.conversions.units

import com.yanivsos.conversions.units.parser.DecimalParser
import com.yanivsos.conversions.units.parser.FractionNumberParser
import com.yanivsos.conversions.units.parser.NumberConverter
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

/*
*
*   1 2/3 oz
    1/3 oz
    1 - 2 oz
    1/3 - 1/4 oz
    1/2 oz white
*
* */
class FluidUnitsConverter(
    numberConverter: NumberConverter<FluidUnits>
) {
    private val fractionNumberParser = FractionNumberParser(numberConverter)
    private val decimalParser = DecimalParser(numberConverter)

    fun parseTo(measurement: String, dstMeasurementUnit: FluidUnits): String {
        val srcDrinkUnit = measurement.parseDrinkUnit() ?: return measurement

        val fluidUnitParser = FluidUnitParser(srcDrinkUnit)
        val replacedMeasurement =
            fluidUnitParser.replaceMeasurement(measurement, dstMeasurementUnit)

        return if (decimalParser.containsMatch(replacedMeasurement)) {
            decimalParser.convertMeasurement(replacedMeasurement, srcDrinkUnit, dstMeasurementUnit)

        } else {
            fractionNumberParser.convertMeasurement(
                replacedMeasurement,
                srcDrinkUnit,
                dstMeasurementUnit
            )
        }
    }


    private fun String.parseDrinkUnit(): FluidUnits? {
        fluidUnits.forEach { drinkUnit ->
            if (FluidUnitParser(drinkUnit).parseUnit(this) != null) return drinkUnit
        }
        return null
    }


    private val fluidUnits = listOf(
        FluidUnits.Ml,
        FluidUnits.Cl,
        FluidUnits.Oz,
        FluidUnits.Gallon,
        FluidUnits.Liter,
        FluidUnits.Pint,
        FluidUnits.Quart
    )

}
