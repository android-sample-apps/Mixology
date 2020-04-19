@file:Suppress("RemoveExplicitTypeArguments")

package com.zemingo.cocktailmenu.di

import androidx.room.Room
import com.zemingo.cocktailmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.cocktailmenu.room.CategoryDao
import com.zemingo.cocktailmenu.room.DrinksDatabase
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