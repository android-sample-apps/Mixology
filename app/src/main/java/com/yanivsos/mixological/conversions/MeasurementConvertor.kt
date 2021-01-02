package com.yanivsos.mixological.conversions

fun Double.ozToMl(): Double {
    return this.times(30)
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

fun DrinkUnit.convertTo(drinkUnit: DrinkUnit, value: Double): Double {
    return when (this) {
        DrinkUnit.Oz -> DrinkUnit.Oz.toUnit(value, drinkUnit)
        DrinkUnit.Cl -> DrinkUnit.Cl.toUnit(value, drinkUnit)
        DrinkUnit.Ml -> DrinkUnit.Ml.toUnit(value, drinkUnit)
    }
}

fun DrinkUnit.Oz.toUnit(value: Double, unit: DrinkUnit): Double {
    return when (unit) {
        DrinkUnit.Oz -> value
        DrinkUnit.Cl -> value.ozToCl()
        DrinkUnit.Ml -> value.ozToMl()
    }
}

fun DrinkUnit.Ml.toUnit(value: Double, unit: DrinkUnit): Double {
    return when (unit) {
        DrinkUnit.Oz -> value.mlToOz()
        DrinkUnit.Cl -> value.mlToCl()
        DrinkUnit.Ml -> value
    }
}

fun DrinkUnit.Cl.toUnit(value: Double, unit: DrinkUnit): Double {
    return when (unit) {
        DrinkUnit.Oz -> value.clToOz()
        DrinkUnit.Cl -> value
        DrinkUnit.Ml -> value.clToMl()
    }
}
