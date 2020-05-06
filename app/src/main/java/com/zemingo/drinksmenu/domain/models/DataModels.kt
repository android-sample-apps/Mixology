package com.zemingo.drinksmenu.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientModel(
    @PrimaryKey val name: String
)

@Entity
data class IngredientDetailsModel(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val isAlcoholic: Boolean,
    val image: String
) {
    private fun shortDescription(): String? {
        return description?.take(10)?.let {
            "$it..."
        }
    }

    fun debugPrint(): String {
        return "IngredientDetailsModel(id=$id, name=$name, description=${shortDescription()}, isAlcoholic=$isAlcoholic, image=$image"
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
    val thumbnail: String?
)

@Entity
data class PreviousSearchModel(
    @PrimaryKey val drinkId: String,
    val lastViewedTime: Long
)

data class SearchResultModel(
    @Embedded val searchModel: PreviousSearchModel,
    @Embedded val resultModel: DrinkPreviewModel
)

data class DrinkModel(
    @PrimaryKey val id: String,
    val name: String,
    val instructions: String,
    val ingredients: Map<String, String>,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val video: String?,
    val thumbnail: String?
) {

    fun toStringUi(): String {
        return "id: $id\n\n$name\n\n$instructions\n\n$ingredients"
    }
}
