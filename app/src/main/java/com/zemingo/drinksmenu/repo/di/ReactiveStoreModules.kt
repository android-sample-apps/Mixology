@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.drinksmenu.repo.di

import androidx.room.Room
import com.zemingo.drinksmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.IngredientReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.SearchDrinkPreviewReactiveStore
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

    factory<SearchDrinkPreviewDao> { get<DrinksDatabase>().searchesDrinkPreviewDao() }

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
        DrinkPreviewReactiveStore(
            drinkPreviewDao = get<DrinkPreviewDao>()
        )
    }

    factory {
        SearchDrinkPreviewReactiveStore(
            searchDrinkPreviewDao = get<SearchDrinkPreviewDao>()
        )
    }
}