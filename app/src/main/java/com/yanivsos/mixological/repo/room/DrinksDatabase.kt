package com.yanivsos.mixological.repo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yanivsos.mixological.domain.models.*
import com.yanivsos.mixological.v2.categories.dao.CategoriesDao
import com.yanivsos.mixological.v2.favorites.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.landingPage.dao.LandingPageDao

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
    version = 3
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
    abstract fun drinkDaoV2(): com.yanivsos.mixological.v2.drink.dao.DrinkDao
    abstract fun favoriteDaoV2(): FavoriteDrinksDao
    abstract fun getLandingPageDao(): LandingPageDao
    abstract fun getCategoriesDao(): CategoriesDao
    abstract fun getIngredientDao(): com.yanivsos.mixological.v2.ingredients.dao.IngredientDao

}

