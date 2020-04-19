@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.drinksmenu.di

import androidx.room.Room
import com.zemingo.drinksmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.drinksmenu.room.CategoryDao
import com.zemingo.drinksmenu.room.DrinksDatabase
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

    factory {
        CategoryReactiveStore(
            categoryDao = get<CategoryDao>()
        )
    }
}