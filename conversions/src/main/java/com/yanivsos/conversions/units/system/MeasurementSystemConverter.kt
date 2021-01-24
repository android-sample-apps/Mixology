package com.yanivsos.conversions.units.system

import com.yanivsos.conversions.units.MeasureUnit

interface MeasurementSystemConverter<T: MeasureUnit> {
    fun toMetricUnit(src: T): T
    fun toImperialUnit(src: T): T
}
