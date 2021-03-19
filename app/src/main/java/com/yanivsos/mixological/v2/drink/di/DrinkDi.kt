package com.yanivsos.mixological.v2.drink.di

import com.yanivsos.mixological.repo.room.DrinksDatabase
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.drink.useCases.FetchAndStoreDrinkUseCase
import com.yanivsos.mixological.v2.drink.useCases.GetDrinkUseCase
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkUseCase
import com.yanivsos.mixological.v2.drink.viewModel.DrinkErrorViewModel
import com.yanivsos.mixological.v2.drink.viewModel.DrinkViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val drinkDi = module {

    single { get<DrinksDatabase>().drinkDaoV2() }

    single {
        DrinkRepository(
            drinkService = get(),
            drinkDao = get(),
            favoriteDrinksDao = get()
        )
    }

    single {
        FetchAndStoreDrinkUseCase(
            repo = get()
        )
    }

    factory { (drinkId: String) ->
        GetDrinkUseCase(
            drinkId = drinkId,
            drinkRepository = get()
        )
    }

    factory { (drinkId: String) ->
        GetOrFetchDrinkUseCase(
            getDrinkUseCase = get { parametersOf(drinkId) },
            fetchAndStoreDrinkUseCase = get()
        )
    }

    viewModel { (drinkId: String) ->
        DrinkViewModel(
            application = androidApplication(),
            getOrFetchDrinkUseCase = get { parametersOf(drinkId) },
            toggleFavoriteUseCase = get()
        )
    }

    viewModel {
        DrinkErrorViewModel(
            fetchAndStoreDrinkUseCase = get()
        )
    }
}
