package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.domain.*
import org.koin.dsl.module

val useCasesModule = module {

    factory {
        GetCategoriesUseCase(
            repository = get()
        )
    }

    factory {
        FetchIngredientsUseCase(
            repository = get()
        )
    }

    factory {
        GetIngredientsUseCase(
            repository = get()
        )
    }

    factory { (category: String) ->
        GetDrinkPreviewByCategoryUseCase(
            repository = get(),
            category = category
        )
    }

    factory {
        GetDrinkPreviewUseCase(
            repository = get()
        )
    }

    factory {
        GetPreviousSearchResultsUseCase(
            repository = get()
        )
    }

    factory {
        MarkAsSearchedDrinkPreviewUseCase()
    }
}