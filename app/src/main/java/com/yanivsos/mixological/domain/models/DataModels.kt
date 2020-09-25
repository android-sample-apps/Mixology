package com.yanivsos.mixological.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yanivsos.mixological.repo.room.*

@Entity(tableName = TABLE_NAME_INGREDIENTS)
data class IngredientModel(
    @PrimaryKey val name: String
)

@Entity(tableName = TABLE_NAME_GLASSES)
data class GlassModel(
    @PrimaryKey val name: String
)

@Entity(tableName = TABLE_NAME_ALCOHOLIC_FILTERS)
data class AlcoholicFilterModel(
    @PrimaryKey val name: String
)

@Entity(tableName = TABLE_NAME_WATCHLIST)
data class WatchlistItemModel(
    @PrimaryKey val id: String
)

@Entity(tableName = TABLE_NAME_INGREDIENTS_DETAILS)
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

@Entity(tableName = TABLE_NAME_CATEGORY)
data class CategoryModel(
    @PrimaryKey val name: String
)

data class CategoryWithImageModel(
    val categoryModel: CategoryModel,
    val image: String?
)

@Entity(tableName = TABLE_NAME_DRINK_PREVIEWS)
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
        false
    )
}

@Entity(tableName = TABLE_NAME_LATEST_ARRIVALS)
data class LatestArrivalsModel(
    @PrimaryKey val drinkId: String
)

@Entity(tableName = TABLE_NAME_MOST_POPULAR)
data class MostPopularModel(
    @PrimaryKey val drinkId: String
)

@Entity(tableName = TABLE_NAME_RECENTLY_VIEWED)
data class RecentlyViewedModel(
    @PrimaryKey val drinkId: String,
    val lastViewedTime: Long
)

@Entity(tableName = TABLE_NAME_DRINKS)
data class DrinkModel(
    @PrimaryKey val id: String,
    val name: String,
    val nameLocalsMap: Map<String, String?>,
    val instructions: String,
    val instructionsLocalsMap: Map<String, String?>,
    val ingredients: Map<String, String?>,
    val category: String,
    val alcoholic: String?,
    val glass: String,
    val video: String?,
    val thumbnail: String?,
    val isFavorite: Boolean = false
)

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Loading<T>(val id: String) : Result<T>()
    data class Error<T>(val tr: Throwable) : Result<T>()
}

data class SearchFiltersModel(
    val categories: List<CategoryModel> = emptyList(),
    val alcoholic: List<AlcoholicFilterModel> = emptyList(),
    val ingredients: List<IngredientModel> = emptyList(),
    val glasses: List<GlassModel> = emptyList()
)