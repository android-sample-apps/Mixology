package com.zemingo.drinksmenu.models

data class DrinkItemUiModel(
    val name: String,
    val ingredients: String,
    val glassIcon: Int,
    val thumbnail: ImageModel?
)

data class CategoryUiModel(
    val name: String
)

data class DrinkPreviewUiModel(
    val id: String,
    val name: String,
    val thumbnail: String?
)

data class DrinksPreviewListItemUiModel(
    val drinks: List<DrinkPreviewUiModel>
)