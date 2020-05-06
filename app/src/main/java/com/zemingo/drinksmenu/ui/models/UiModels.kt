package com.zemingo.drinksmenu.ui.models

import android.text.SpannableString
import android.text.SpannableStringBuilder

data class CategoryUiModel(
    val name: String
)

data class DrinkPreviewUiModel(
    val id: String,
    val name: String,
    val thumbnail: String?
)

data class DrinkUiModel(
    val id: String,
    val name: String,
    val instructions: List<SpannableString>,
    val ingredients: List<IngredientUiModel>,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val video: String?,
    val thumbnail: String?
)

data class LandingPageUiModel(
    val mostPopular: List<DrinkPreviewUiModel>,
    val latestArrivals: List<DrinkPreviewUiModel>,
    val recentSearches: List<DrinkPreviewUiModel>
)

data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val thumbnail: String? = null
)

data class IngredientDetailsUiModel(
    val name: String,
    val description: String?,
    val image: String,
    val isAlcoholic: Boolean
)