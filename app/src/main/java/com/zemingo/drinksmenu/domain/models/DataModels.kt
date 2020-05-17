package com.zemingo.drinksmenu.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientModel(
    @PrimaryKey val name: String
)

@Entity
data class GlassModel(
    @PrimaryKey val name: String
)

@Entity
data class AlcoholicFilterModel(
    @PrimaryKey val name: String
)

@Entity
data class WatchlistItemModel(
    @PrimaryKey val id: String
)

@Entity
data class IngredientDetailsModel(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val isAlcoholic: Boolean,
    val image: String,
    val alcoholVolume: Int?
) {
    private fun shortDescription(): String? {
        return description?.take(10)?.let {
            "$it..."
        }
    }

    fun debugPrint(): String {
        return "IngredientDetailsModel(id=$id, name=$name, description=${shortDescription()}, isAlcoholic=$isAlcoholic, ABV= ${alcoholVolume},image=$image"
    }
}

@Entity
data class CategoryModel(
    @PrimaryKey val name: String
)

@Entity
data class DrinkPreviewModel(
    @PrimaryKey val id: String,
    val name: String,
    val thumbnail: String?,
    val isFavorite: Boolean
) {
    constructor(drinkModel: DrinkModel) : this(
        drinkModel.id,
        drinkModel.name,
        drinkModel.thumbnail,
        drinkModel.isFavorite
    )
}

@Entity
data class RecentlyViewedModel(
    @PrimaryKey val drinkId: String,
    val lastViewedTime: Long
)

data class DrinkModel(
    @PrimaryKey val id: String,
    val name: String,
    val instructions: String,
    val ingredients: Map<String, String?>,
    val category: String,
    val alcoholic: String?,
    val glass: String,
    val video: String?,
    val thumbnail: String?,
    val isFavorite: Boolean
)

data class SearchFiltersModel(
    val categories: List<CategoryModel> = emptyList(),
    val alcoholic: List<AlcoholicFilterModel> = emptyList(),
    val ingredients: List<IngredientModel> = emptyList(),
    val glasses: List<GlassModel> = emptyList()
)