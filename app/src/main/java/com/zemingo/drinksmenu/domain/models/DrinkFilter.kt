package com.zemingo.drinksmenu.domain.models

enum class FilterType {
    ALCOHOL, CATEGORY, GLASS, INGREDIENTS
}

data class DrinkFilter(
    val query: String,
    val type: FilterType
)