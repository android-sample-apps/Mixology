package com.zemingo.drinksmenu.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class InstructionModel(
    val instructions: String
)

data class IngredientModel(
    val ingredient: String,
    val measurement: String
)

data class IngredientListModel(
    val ingredients: List<IngredientModel>
)

data class ImageModel(
    val link: String
)

data class DateModel(
    val date: String
)

data class DrinkModel(
    val id: String,
    val name: String,
    val instructions: InstructionModel,
    val ingredients: IngredientListModel,
    val thumbnail: ImageModel?,
    val dateModified: DateModel
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
