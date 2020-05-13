package com.zemingo.drinksmenu.domain.models

enum class FilterType {
    ALCOHOL, CATEGORY, GLASS, INGREDIENTS, NAME
}

data class DrinkFilter(
    val query: String,
    val type: FilterType,
    val active: Boolean = true
)