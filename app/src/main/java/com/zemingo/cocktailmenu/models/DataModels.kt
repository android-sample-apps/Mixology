package com.zemingo.cocktailmenu.models

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

data class DrinkPreviewModel(
    val id: String,
    val name: String,
    val thumbnail: ImageModel?
)

data class DrinkModel(
    val id: String,
    val name: String,
    val instructions: InstructionModel,
    val ingredients: IngredientListModel,
    val thumbnail: ImageModel?,
    val dateModified: DateModel
)

data class DrinkPreviewListModel(
    val drinks: List<DrinkPreviewModel>
)

@Entity
data class CategoryEntity(
    @PrimaryKey val name: String
)