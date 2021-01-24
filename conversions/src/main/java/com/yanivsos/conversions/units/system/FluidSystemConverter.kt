package com.yanivsos.conversions.units.system

import com.yanivsos.conversions.units.FluidUnits

class FluidSystemConverter: MeasurementSystemConverter<FluidUnits> {

    override fun toMetricUnit(src: FluidUnits): FluidUnits {
        return when (src) {
            FluidUnits.Oz -> FluidUnits.Ml
            FluidUnits.Cl -> FluidUnits.Ml
            FluidUnits.Ml -> FluidUnits.Ml
            FluidUnits.Quart -> FluidUnits.Liter
            FluidUnits.Liter -> FluidUnits.Liter
            FluidUnits.Gallon -> FluidUnits.Liter
            FluidUnits.Pint -> FluidUnits.Ml
        }
    }

    override fun toImperialUnit(src: FluidUnits): FluidUnits {
        return when (src) {
            FluidUnits.Oz -> FluidUnits.Oz
            FluidUnits.Cl -> FluidUnits.Oz
            FluidUnits.Ml -> FluidUnits.Oz
            FluidUnits.Quart -> FluidUnits.Quart
            FluidUnits.Liter -> FluidUnits.Quart
            FluidUnits.Gallon -> FluidUnits.Gallon
            FluidUnits.Pint -> FluidUnits.Pint
        }
    }
}
