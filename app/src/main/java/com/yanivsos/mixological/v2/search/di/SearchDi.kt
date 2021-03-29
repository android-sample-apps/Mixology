package com.yanivsos.mixological.v2.search.di

import com.yanivsos.mixological.v2.search.repo.SearchRepository
import com.yanivsos.mixological.v2.search.useCases.*
import com.yanivsos.mixological.v2.search.viewModel.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchDi = module {

    single {
        GetAutoCompleteSuggestionsUseCase(
            drinkRepository = get()
        )
    }

    single {
        SearchRepository(
            ingredientDao = get(),
            alcoholicFilterDao = get(),
            categoryDao = get(),
            glassDao = get()
        )
    }

    single {
        FindSimilarIngredientsByNameUseCase(
            searchRepository = get()
        )
    }

    single {
        GetAllFiltersUseCase(
            searchRepository = get()
        )
    }

    single {
        FetchDrinkByNameUseCase(
            drinkRepository = get()
        )
    }

    factory {
        AlcoholicFilterUseCase(
            drinkRepository = get(),
        )
    }

    factory {
        CategoryFilterUseCase(
            drinkRepository = get(),
        )
    }

    factory {
        GlassFilterUseCase(
            drinkRepository = get(),
        )

    }

    factory {
        IngredientsFilterUseCase(
            drinkRepository = get()
        )
    }

    factory {
        SearchDrinksUseCase(
            drinkRepository = get(),
            getAllFiltersUseCase = get(),
            alcoholicFilterByUseCase = get(),
            glassFilterByUseCase = get(),
            categoriesFilterByUseCase = get(),
            ingredientsFilterUseCase = get(),
            fetchDrinkByNameUseCase = get()
        )
    }

    viewModel {
        SearchViewModel(
            getAutoCompleteSuggestionsUseCase = get(),
            searchDrinksUseCase = get(),
            findSimilarIngredientsByNameUseCase = get()
        )
    }
}
