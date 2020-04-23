package com.zemingo.drinksmenu.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.models.IngredientModel
import com.zemingo.drinksmenu.models.PreviousSearchModel

@Database(
    entities = [
        CategoryModel::class,
        DrinkPreviewModel::class,
        IngredientModel::class,
        PreviousSearchModel::class
    ],
    version = 1
)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun drinkPreviewDao(): DrinkPreviewDao
    abstract fun ingredientDao(): IngredientDao
}