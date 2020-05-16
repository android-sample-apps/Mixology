package com.zemingo.drinksmenu.repo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zemingo.drinksmenu.domain.models.*

@Database(
    entities = [
        CategoryModel::class,
        DrinkPreviewModel::class,
        IngredientModel::class,
        PreviousSearchModel::class,
        IngredientDetailsModel::class,
        GlassModel::class,
        AlcoholicFilterModel::class,
        WatchlistItemModel::class
    ],
    version = 1
)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun drinkPreviewDao(): DrinkPreviewDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun glassDao(): GlassDao
    abstract fun alcoholicFiltersDao(): AlcoholicFilterDao
    abstract fun searchesDrinkPreviewDao(): SearchDrinkPreviewDao
    abstract fun ingredientDetailsDao(): IngredientDetailsDao
    abstract fun watchlistDao(): WatchlistDao

}

