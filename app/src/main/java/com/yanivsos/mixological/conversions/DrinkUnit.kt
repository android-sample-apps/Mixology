package com.yanivsos.mixological.conversions

sealed class DrinkUnit(val name: String) {
    object Oz : DrinkUnit("oz")
    object Cl : DrinkUnit("cl")
    object Ml : DrinkUnit("ml")
}
