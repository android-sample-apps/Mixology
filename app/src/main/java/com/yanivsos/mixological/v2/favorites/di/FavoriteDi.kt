package com.yanivsos.mixological.v2.favorites.di

import com.yanivsos.mixological.database.DrinksDatabase
import com.yanivsos.mixological.v2.favorites.useCases.AddToFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.GetFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.RemoveFromFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.ToggleFavoriteUseCase
import com.yanivsos.mixological.v2.favorites.viewModel.FavoritesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteDi = module {

    single { get<DrinksDatabase>().getFavoriteDao() }

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

    factory {
        GetFavoritesUseCase(
            repository = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            getFavoritesUseCase = get()
        )
    }

}
