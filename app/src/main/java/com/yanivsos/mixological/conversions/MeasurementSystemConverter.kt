package com.yanivsos.mixological.conversions

class MeasurementSystemConverter(
    private val system: MeasurementSystem = MeasurementSystem.Metric,
) {
    private val fractionNumberParser = FractionNumberParser()
    private val decimalParser = DecimalParser()

}
