package com.zemingo.drinksmenu.models

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

data class DrinkModel(
    @PrimaryKey val id: String,
    val name: String,
    val instructions: String,
    val ingredients: Map<String, String>,
    val thumbnail: String?) {

    fun toStringUi(): String {
        return "id: $id\n\n$name\n\n$instructions\n\n$ingredients"
    }
}
