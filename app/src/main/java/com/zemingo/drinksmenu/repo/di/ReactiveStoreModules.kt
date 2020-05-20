@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.drinksmenu.repo.di

import androidx.room.Room
import com.zemingo.drinksmenu.repo.reactiveStore.*
import com.zemingo.drinksmenu.repo.room.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val reactiveStoreModule = module {

    single<DrinksDatabase> {
        Room.databaseBuilder(
            androidContext(),
            DrinksDatabase::class.java, "drinks-db"
        ).build()
    }

    factory<CategoryDao> { get<DrinksDatabase>().categoryDao() }

    factory<DrinkPreviewDao> { get<DrinksDatabase>().drinkPreviewDao() }

    factory<IngredientDao> { get<DrinksDatabase>().ingredientDao() }

    factory<RecentlyViewedDao> { get<DrinksDatabase>().recentlyViewedDao() }

    factory<IngredientDetailsDao> { get<DrinksDatabase>().ingredientDetailsDao() }

    factory<AlcoholicFilterDao> { get<DrinksDatabase>().alcoholicFiltersDao() }

    factory<GlassDao> { get<DrinksDatabase>().glassDao() }

    factory<WatchlistDao> { get<DrinksDatabase>().watchlistDao() }

    factory<DrinkDao> { get<DrinksDatabase>().drinkDao() }

    factory {
        CategoryReactiveStore(
            categoryDao = get<CategoryDao>()
        )
    }

    factory {
        IngredientReactiveStore(
            ingredientDao = get<IngredientDao>()
        )
    }

    factory {
        GlassReactiveStore(
            glassDao = get<GlassDao>()
        )
    }

    factory {
        AlcoholicFiltersReactiveStore(
            alcoholicFilterDao = get<AlcoholicFilterDao>()
        )
    }

    factory {
        DrinkPreviewReactiveStore(
            drinkPreviewDao = get<DrinkPreviewDao>()
        )
    }

    factory {
        RecentlyViewedReactiveStore(
            recentlyViewedDao = get<RecentlyViewedDao>()
        )
    }

    factory {
        IngredientDetailsReactiveStore(
            ingredientDetailsDao = get<IngredientDetailsDao>()
        )
    }

    factory {
        WatchlistReactiveStore(
            watchlistDao = get()
        )
    }

    factory {
        DrinkReactiveStore(
            drinksDao = get()
        )
    }
}