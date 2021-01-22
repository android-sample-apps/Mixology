package com.yanivsos.mixological.conversions

import timber.log.Timber

fun Double.ozToMl(): Double {
    return this.times(30)
}

fun Double.qtToOz(): Double {
    return this.times(32)
}

fun Double.ozToCl(): Double {
    return this.ozToMl().mlToCl()
}

fun Double.mlToOz(): Double {
    return this.div(30)
}

fun Double.clToMl(): Double {
    return this.times(10)
}

fun Double.mlToCl(): Double {
    return this.div(10)
}

fun Double.clToOz(): Double {
    return clToMl().mlToOz()
}

fun Double.galToLiter(): Double {
    return this.times(3.78543)
}

fun Double.pintToMl(): Double {
    return this.times(16).ozToMl()
}

fun MeasurementUnit.convertTo(measurementUnit: MeasurementUnit, value: Double): Double {
    return when (this) {
        MeasurementUnit.Oz -> MeasurementUnit.Oz.toUnit(value, measurementUnit)
        MeasurementUnit.Cl -> MeasurementUnit.Cl.toUnit(value, measurementUnit)
        MeasurementUnit.Ml -> MeasurementUnit.Ml.toUnit(value, measurementUnit)
//        MeasurementUnit.Gr -> TODO()
        MeasurementUnit.Quart -> TODO()
        MeasurementUnit.Liter -> TODO()
        MeasurementUnit.Gallon -> TODO()
        MeasurementUnit.Pint -> TODO()
    }
}

fun MeasurementUnit.Oz.toUnit(value: Double, unit: MeasurementUnit): Double {
    return when (unit) {
        MeasurementUnit.Oz -> value
        MeasurementUnit.Cl -> value.ozToCl()
        MeasurementUnit.Ml -> value.ozToMl()
        MeasurementUnit.Quart -> TODO()
        MeasurementUnit.Liter -> TODO()
        MeasurementUnit.Gallon -> TODO()
        MeasurementUnit.Pint -> TODO()
//        MeasurementUnit.Gr -> TODO()
    }
}

fun MeasurementUnit.Ml.toUnit(value: Double, unit: MeasurementUnit): Double {
    return when (unit) {
        MeasurementUnit.Oz -> value.mlToOz()
        MeasurementUnit.Cl -> value.mlToCl()
        MeasurementUnit.Ml -> value
//        MeasurementUnit.Gr -> TODO()
        MeasurementUnit.Quart -> TODO()
        MeasurementUnit.Liter -> TODO()
        MeasurementUnit.Gallon -> TODO()
        MeasurementUnit.Pint -> TODO()
    }
}

fun MeasurementUnit.Cl.toUnit(value: Double, unit: MeasurementUnit): Double {
    return when (unit) {
        MeasurementUnit.Oz -> value.clToOz()
        MeasurementUnit.Cl -> value
        MeasurementUnit.Ml -> value.clToMl()
//        MeasurementUnit.Gr -> TODO()
        MeasurementUnit.Quart -> TODO()
        MeasurementUnit.Liter -> TODO()
        MeasurementUnit.Gallon -> TODO()
        MeasurementUnit.Pint -> TODO()
    }
}


fun MeasurementUnit.toMetric(value: Double): MeasurementConversion {
    return when(this) {
        MeasurementUnit.Oz -> MeasurementUnit.Oz.toUnit(value, MeasurementUnit.Ml).withUnit(MeasurementUnit.Ml)
        MeasurementUnit.Cl -> MeasurementUnit.Cl.toUnit(value, MeasurementUnit.Ml).withUnit(MeasurementUnit.Ml)
        MeasurementUnit.Ml -> value.withUnit(MeasurementUnit.Ml)
//        MeasurementUnit.Gr -> value.withUnit(MeasurementUnit.Gr)
        MeasurementUnit.Quart -> value.qtToOz().ozToMl().withUnit(MeasurementUnit.Ml)
        MeasurementUnit.Liter -> value.withUnit(MeasurementUnit.Liter)
        MeasurementUnit.Gallon -> value.galToLiter().withUnit(MeasurementUnit.Liter)
        MeasurementUnit.Pint -> value.pintToMl().withUnit(MeasurementUnit.Ml)
    }.also {
        Timber.d("toMetric: from [$this], value[$value] = $it ")
    }
}

fun Double.withUnit(unit: MeasurementUnit): MeasurementConversion {
    return MeasurementConversion(
        value = this.prettyDouble(),
        dstUnit = unit
    )
}

data class MeasurementConversion(
    val value: String,
    val dstUnit: MeasurementUnit
)
