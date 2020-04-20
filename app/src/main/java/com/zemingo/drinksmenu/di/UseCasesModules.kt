package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.domain.GetCategoriesUseCase
import com.zemingo.drinksmenu.domain.GetDrinkPreviewByCategoryUseCase
import org.koin.dsl.module

val useCasesModule = module {

    factory { GetCategoriesUseCase(get()) }

    factory { (category: String) ->
        GetDrinkPreviewByCategoryUseCase(
            repo = get(),
            category = category
        )
    }
}