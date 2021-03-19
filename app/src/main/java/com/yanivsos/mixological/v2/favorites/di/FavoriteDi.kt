package com.yanivsos.mixological.v2.favorites.di

import com.yanivsos.mixological.repo.room.DrinksDatabase
import com.yanivsos.mixological.v2.favorites.useCases.AddToFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.RemoveFromFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.ToggleFavoriteUseCase
import org.koin.dsl.module

val favoriteDi = module {

    single { get<DrinksDatabase>().favoriteDaoV2() }

    factory {
        AddToFavoritesUseCase(
            repository = get(),
            fetchAndStoreDrinkUseCase = get()
        )
    }

    factory {
        RemoveFromFavoritesUseCase(
            drinkRepository = get()
        )
    }

    single {
        ToggleFavoriteUseCase(
            drinkRepository = get(),
            addToFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get()
        )
    }
}
