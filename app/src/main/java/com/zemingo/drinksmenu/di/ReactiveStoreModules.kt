@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.drinksmenu.di

import androidx.room.Room
import com.zemingo.drinksmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.IngredientReactiveStore
import com.zemingo.drinksmenu.room.CategoryDao
import com.zemingo.drinksmenu.room.DrinkPreviewDao
import com.zemingo.drinksmenu.room.DrinksDatabase
import com.zemingo.drinksmenu.room.IngredientDao
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
}