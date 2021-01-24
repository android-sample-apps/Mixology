package com.yanivsos.conversions.units.system

import com.yanivsos.conversions.units.FluidUnits

class FluidSystemConverter: MeasurementSystemConverter<FluidUnits> {

    override fun toMetricUnit(src: FluidUnits): FluidUnits {
        return when (src) {
            FluidUnits.Oz -> FluidUnits.Milliliter
            FluidUnits.Centiliter -> FluidUnits.Milliliter
            FluidUnits.Milliliter -> FluidUnits.Milliliter
            FluidUnits.Quart -> FluidUnits.Milliliter
            FluidUnits.Liter -> FluidUnits.Liter
            FluidUnits.Gallon -> FluidUnits.Liter
            FluidUnits.Pint -> FluidUnits.Milliliter
        }
    }

    override fun toImperialUnit(src: FluidUnits): FluidUnits {
        return when (src) {
            FluidUnits.Oz -> FluidUnits.Oz
            FluidUnits.Centiliter -> FluidUnits.Oz
            FluidUnits.Milliliter -> FluidUnits.Oz
            FluidUnits.Quart -> FluidUnits.Quart
            FluidUnits.Liter -> FluidUnits.Quart
            FluidUnits.Gallon -> FluidUnits.Gallon
            FluidUnits.Pint -> FluidUnits.Pint
        }
    }
}
