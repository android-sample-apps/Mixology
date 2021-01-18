package com.yanivsos.mixological.conversions

sealed class MeasurementSystem {
    object Imperial: MeasurementSystem()
    object Metric: MeasurementSystem()
}
