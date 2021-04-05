package com.yanivsos.mixological.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yanivsos.mixological.v2.search.dao.AlcoholicFilterDao
import com.yanivsos.mixological.v2.search.dao.CategoryDao
import com.yanivsos.mixological.v2.search.dao.GlassDao
import com.yanivsos.mixological.v2.categories.dao.CategoriesDao
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.favorites.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.ingredients.dao.IngredientDao
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
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getGlassDao(): GlassDao
    abstract fun getAlcoholicFilters(): AlcoholicFilterDao
    abstract fun getDrinkDao(): DrinkDao
    abstract fun getFavoriteDao(): FavoriteDrinksDao
    abstract fun getLandingPageDao(): LandingPageDao
    abstract fun getCategoriesDao(): CategoriesDao
    abstract fun getIngredientDao(): IngredientDao

}

