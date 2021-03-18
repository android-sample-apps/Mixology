package com.yanivsos.mixological.v2.drink.di

import com.yanivsos.mixological.repo.room.DrinksDatabase
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.drink.useCases.FetchAndStoreDrinkUseCase
import com.yanivsos.mixological.v2.drink.useCases.GetDrinkUseCase
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkUseCase
import com.yanivsos.mixological.v2.drink.viewModel.DrinkErrorViewModel
import com.yanivsos.mixological.v2.drink.viewModel.DrinkViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val drinkDi = module {

    single { get<DrinksDatabase>().drinkDaoV2() }

    single { get<DrinksDatabase>().favoriteDaoV2() }

    factory {
        DrinkRepository(
            drinkService = get(),
            drinkDao = get(),
            favoriteDrinksDao = get()
        )
    }

    factory {
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
            context = androidContext(),
            getOrFetchDrinkUseCase = get { parametersOf(drinkId) },
            toggleWatchlistUseCase = get()
        )
    }

    viewModel {
        DrinkErrorViewModel(
            fetchAndStoreDrinkUseCase = get()
        )
    }
}
