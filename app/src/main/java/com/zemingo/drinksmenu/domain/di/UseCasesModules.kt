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
            combineWithFavoriteUseCase = get()
        )
    }

    factory {
        GetRecentlyViewedUseCase(
            combineWithFavoriteUseCase = get(),
            recentlyViewedRepository = get()
        )
    }

    factory {
        AddToRecentlyViewedUseCase(
            repository = get()
        )
    }

    factory {
        GetMostPopularUseCase(
            combineWithFavoriteUseCase = get(),
            repository = get()
        )
    }

    factory {
        GetLatestArrivalsUseCase(
            combineWithFavoriteUseCase = get(),
            repository = get()
        )
    }

    factory { (drinkId: String) ->
        GetDrinkUseCase(
            fetchAndStoreDrinkUseCase = get(),
            getWatchlistUseCase = get(),
            repository = get(),
            drinkId = drinkId
        )
    }

    factory {
        FetchAndStoreDrinkUseCase(
            repository = get()
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
            drinkPreviewRepository = get(),
            combineWithFavoriteUseCase = get(),
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
            fetchAndStoreDrinkUseCase = get(),
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

    factory {
        CombineWithFavoriteUseCase(
            watchlistRepository = get()
        )
    }

    factory {
        ToggleWatchlistUseCase(
            getWatchlistUseCase = get(),
            addToWatchlistUseCase = get(),
            removeFromWatchlistUseCase = get()
        )
    }

}