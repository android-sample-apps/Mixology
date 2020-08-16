package com.yanivsos.mixological.repo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yanivsos.mixological.domain.models.*

@Database(
    entities = [
        CategoryModel::class,
        DrinkPreviewModel::class,
        DrinkModel::class,
        IngredientModel::class,
        RecentlyViewedModel::class,
        IngredientDetailsModel::class,
        GlassModel::class,
        AlcoholicFilterModel::class,
        WatchlistItemModel::class,
        LatestArrivalsModel::class,
        MostPopularModel::class
    ],
    version = 2
)
@TypeConverters(MapStringToStringOptionalConverter::class)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun drinkPreviewDao(): DrinkPreviewDao
    abstract fun drinkDao(): DrinkDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun glassDao(): GlassDao
    abstract fun alcoholicFiltersDao(): AlcoholicFilterDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun ingredientDetailsDao(): IngredientDetailsDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun latestArrivalsDao(): LatestArrivalsDao
    abstract fun mostPopularDao(): MostPopularDao

}

