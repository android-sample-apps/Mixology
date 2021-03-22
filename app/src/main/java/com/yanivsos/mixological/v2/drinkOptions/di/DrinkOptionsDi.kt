package com.yanivsos.mixological.v2.drinkOptions.di

import com.yanivsos.mixological.v2.drinkOptions.DrinkPreviewOptionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val drinkOptionsDi = module {

    viewModel { (drinkId: String) ->
        DrinkPreviewOptionsViewModel(
            addToFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get(),
            getDrinkUseCase = get { parametersOf(drinkId) },
            drinkId = drinkId
        )
    }
}
