package com.zemingo.cocktailmenu.models

data class DrinkItemUiModel(
    val name: String,
    val ingredients: String,
    val glassIcon: Int,
    val thumbnail: ImageModel?
)

data class CategoryUiModel(
    val name: String
)

data class DrinkPreviewItemUiModel(
    val id: String,
    val name: String,
    val thumbnail: ImageModel?
)

data class DrinksPreviewListItemUiModel(
    val drinks: List<DrinkPreviewItemUiModel>
)