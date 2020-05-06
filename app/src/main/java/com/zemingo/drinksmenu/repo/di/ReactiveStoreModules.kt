@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.drinksmenu.repo.di

import androidx.room.Room
import com.zemingo.drinksmenu.repo.reactive_store.*
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

    factory<IngredientDetailsDao> { get<DrinksDatabase>().ingredientDetailsDao() }

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

    factory {
        IngredientDetailsReactiveStore(
            ingredientDetailsDao = get<IngredientDetailsDao>()
        )
    }
}