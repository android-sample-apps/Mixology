package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.domain.*
import org.koin.dsl.module

val useCasesModule = module {

    factory { GetCategoriesUseCase(get()) }

    factory { FetchIngredientsUseCase(get()) }

    factory { GetIngredientsUseCase(get()) }

    factory { (category: String) ->
        GetDrinkPreviewByCategoryUseCase(
            repo = get(),
            category = category
        )
    }

    factory { GetDrinkPreviewUseCase(get()) }
}