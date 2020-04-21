package com.zemingo.drinksmenu.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
