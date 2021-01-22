package com.yanivsos.mixological.conversions

sealed class MeasurementUnit(vararg names: String) {
    val names = names.toSet()

    object Oz : MeasurementUnit("oz")
    object Cl : MeasurementUnit("cl")
    object Ml : MeasurementUnit("ml")

    //TODO - support all of these
    //    1 gr = 0.035274 oz
//    object Gr : MeasurementUnit("gr")

    object Quart : MeasurementUnit("qt", "quart")


    object Liter : MeasurementUnit("L")

    //1 gal = 3.7 Liter
    object Gallon : MeasurementUnit("gal")

    //1 pint = 570 ml
    object Pint : MeasurementUnit("pint")
//    object Lb : MeasurementUnit("lb")
//    object Lbs : MeasurementUnit("lbs")
}
