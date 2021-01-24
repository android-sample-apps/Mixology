package com.yanivsos.conversions.units.system

sealed class MeasurementSystem {
    object Metric: MeasurementSystem()
    object Imperial: MeasurementSystem()
}
