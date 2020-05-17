package com.zemingo.drinksmenu.domain.di

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

    factory {
        GetDrinkPreviewByCategoryUseCase(
            repository = get()
        )
    }

    factory {
        GetDrinkPreviewUseCase(
            repository = get(),
            watchlistRepository = get()
        )
    }

    factory {
        GetPreviousSearchResultsUseCase(
            searchRepository = get()
        )
    }

    factory {
        MarkAsSearchedDrinkPreviewUseCase(
            repository = get()
        )
    }

    factory {
        GetMostPopularUseCase(
            repository = get()
        )
    }

    factory {
        GetLatestArrivalsUseCase(
            repository = get()
        )
    }

    factory { (id: String) ->
        GetDrinkUseCase(
            repository = get(),
            id = id
        )
    }

    factory { (ingredientName: String) ->
        GetIngredientDetailsUseCase(
            repository = get(),
            ingredientName = ingredientName
        )
    }

    factory {
        FilterDrinkUseCase(
            advancedSearchRepository = get()
        )
    }

    factory {
        UnionFilterDrinkUseCase(
            advancedSearchRepository = get()
        )
    }

    factory {
        IntersectionFilterDrinkUseCase(
            advancedSearchRepository = get()
        )
    }

    factory {
        MultipleFilterDrinkUseCase(
            getDrinkPreviewUseCase = get(),
            alcoholicFilter = get<UnionFilterDrinkUseCase>(),
            categoryFilter = get<UnionFilterDrinkUseCase>(),
            ingredientFilter = get<IntersectionFilterDrinkUseCase>(),
            glassFilter = get<UnionFilterDrinkUseCase>(),
            nameFilter = get<FilterDrinkUseCase>()
        )
    }

    factory {
        GetGlassesUseCase(
            repository = get()
        )
    }

    factory {
        GetAlcoholicFiltersUseCase(
            repository = get()
        )
    }

    factory {
        GetSearchFiltersUseCase(
            getAlcoholicFiltersUseCase = get(),
            getCategoriesUseCase = get(),
            getGlassesUseCase = get(),
            getIngredientsUseCase = get()
        )
    }

    factory {
        FetchAllPreviewsUseCase(
            previewRepository = get()
        )
    }

    factory {
        AddToWatchlistUseCase(
            repository = get()
        )
    }

    factory {
        GetWatchlistUseCase(
            watchlistRepository = get(),
            drinkPreviewRepository = get()
        )
    }

    factory {
        RemoveFromWatchlistUseCase(
            repository = get()
        )
    }

}