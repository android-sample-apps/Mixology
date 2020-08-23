package com.yanivsos.mixological.search_autocomplete

data class DrinkAutoCompleteModel(
    val name: String,
    val image: String? = null
)

data class DrinkAutoCompleteUiModel(
    val name: String
)

fun List<DrinkAutoCompleteModel>.toUiModel() =
    map { it.toUiModel() }

fun DrinkAutoCompleteModel.toUiModel() =
    DrinkAutoCompleteUiModel(
        name = this.name
    )