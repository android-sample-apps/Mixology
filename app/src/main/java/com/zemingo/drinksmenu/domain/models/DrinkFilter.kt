package com.zemingo.drinksmenu.domain.models

enum class FilterType {
    ALCOHOL, CATEGORY, GLASS, INGREDIENTS, NAME
}

data class DrinkFilter(
    val query: String,
    val type: FilterType,
    val active: Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DrinkFilter

        if (query != other.query) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = query.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}