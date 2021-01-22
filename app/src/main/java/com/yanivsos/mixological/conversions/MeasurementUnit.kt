package com.yanivsos.mixological.conversions

sealed class MeasurementUnit(vararg names: String) {
    val names = names.toSet()

    abstract fun toOz(d: Double): Double
    abstract fun toCl(d: Double): Double
    abstract fun toMl(d: Double): Double
    abstract fun toQuart(d: Double): Double
    abstract fun toLiter(d: Double): Double
    abstract fun toGallon(d: Double): Double
    abstract fun toPint(d: Double): Double

    fun convertTo(d: Double, measurementUnit: MeasurementUnit): Double {
        return when (measurementUnit) {
            Oz -> toOz(d)
            Cl -> toCl(d)
            Ml -> toMl(d)
            Quart -> toQuart(d)
            Liter -> toLiter(d)
            Gallon -> toGallon(d)
            Pint -> toPint(d)
        }
    }

    object Oz : MeasurementUnit("oz") {
        override fun toOz(d: Double): Double {
            return d
        }

        override fun toCl(d: Double): Double {
            return d.ozToMl().mlToCl()
        }

        override fun toMl(d: Double): Double {
            return d.ozToMl()
        }

        override fun toQuart(d: Double): Double {
            return d.ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d.ozToMl().mlToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d.ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d.ozToPint()
        }
    }

    object Cl : MeasurementUnit("cl") {
        override fun toOz(d: Double): Double {
            return d.clToOz()
        }

        override fun toCl(d: Double): Double {
            return d
        }

        override fun toMl(d: Double): Double {
            return d.clToMl()
        }

        override fun toQuart(d: Double): Double {
            return d.clToOz().ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d.clToMl().mlToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d.clToOz().ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d.clToOz().ozToPint()
        }
    }

    object Ml : MeasurementUnit("ml") {
        override fun toOz(d: Double): Double {
            return d.mlToOz()
        }

        override fun toCl(d: Double): Double {
            return d.mlToCl()
        }

        override fun toMl(d: Double): Double {
            return d
        }

        override fun toQuart(d: Double): Double {
            return d.mlToOz().ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d.mlToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d.mlToOz().ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d.mlToOz().ozToPint()
        }
    }


    object Quart : MeasurementUnit("qt", "quart") {
        override fun toOz(d: Double): Double {
            return d.qtToOz()
        }

        override fun toCl(d: Double): Double {
            return d.qtToOz().ozToCl()
        }

        override fun toMl(d: Double): Double {
            return d.qtToOz().ozToMl()
        }

        override fun toQuart(d: Double): Double {
            return d
        }

        override fun toLiter(d: Double): Double {
            return d.qtToOz().ozToMl().mlToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d.qtToOz().ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d.qtToOz().ozToPint()
        }
    }

    object Liter : MeasurementUnit("L") {
        override fun toOz(d: Double): Double {
            return d.literToMl().mlToOz()
        }

        override fun toCl(d: Double): Double {
            return d.literToMl().mlToCl()
        }

        override fun toMl(d: Double): Double {
            return d.literToMl()
        }

        override fun toQuart(d: Double): Double {
            return d.literToMl().mlToOz().ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d
        }

        override fun toGallon(d: Double): Double {
            return d.literToMl().mlToOz().ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d.literToMl().mlToOz().ozToPint()
        }
    }

    object Gallon : MeasurementUnit("gal") {
        override fun toOz(d: Double): Double {
            return d.galToOz()
        }

        override fun toCl(d: Double): Double {
            return d.galToOz().ozToCl()
        }

        override fun toMl(d: Double): Double {
            return d.galToOz().ozToMl()
        }

        override fun toQuart(d: Double): Double {
            return d.galToOz().ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d.galToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d
        }

        override fun toPint(d: Double): Double {
            return d.galToOz().ozToPint()
        }
    }

    object Pint : MeasurementUnit("pint") {
        override fun toOz(d: Double): Double {
            return d.pintToMl().mlToOz()
        }

        override fun toCl(d: Double): Double {
            return d.pintToMl().mlToCl()
        }

        override fun toMl(d: Double): Double {
            return d.pintToMl()
        }

        override fun toQuart(d: Double): Double {
            return d.pintToMl().mlToOz().ozToQt()
        }

        override fun toLiter(d: Double): Double {
            return d.pintToMl().mlToLiter()
        }

        override fun toGallon(d: Double): Double {
            return d.pintToMl().mlToOz().ozToGallon()
        }

        override fun toPint(d: Double): Double {
            return d
        }
    }


    //TODO - support all of these
    //    1 gr = 0.035274 oz
//    object Gr : MeasurementUnit("gr")
//    object Lb : MeasurementUnit("lb")
//    object Lbs : MeasurementUnit("lbs")
}
