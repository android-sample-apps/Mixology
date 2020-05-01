package com.zemingo.drinksmenu.ui.models

data class CategoryUiModel(
    val name: String
)

data class DrinkPreviewUiModel(
    val id: String,
    val name: String,
    val thumbnail: String?
)

data class LandingPageUiModel(
    val mostPopular: List<DrinkPreviewUiModel>,
    val latestArrivals: List<DrinkPreviewUiModel>,
    val recentSearches: List<DrinkPreviewUiModel>
)