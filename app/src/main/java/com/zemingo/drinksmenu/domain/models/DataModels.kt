package com.zemingo.drinksmenu.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientModel(
    @PrimaryKey val name: String
)

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
    val thumbnail: String?
) {

    fun toStringUi(): String {
        return "id: $id\n\n$name\n\n$instructions\n\n$ingredients"
    }
}
