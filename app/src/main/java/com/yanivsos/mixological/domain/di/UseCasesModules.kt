package com.yanivsos.mixological.domain.di

import com.yanivsos.mixological.domain.*
import org.koin.dsl.module

val useCasesModule = module {

    factory {
        GetCategoriesUseCase(
            repository = get()
        )
    }

    factory {
        GetCategoriesAndImagesUseCase(
            getCategoriesUseCase = get(),
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
        GetIngredientsByNameUseCase(
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
            getDrinkPreviewUseCase = get(),
            repository = get()
        )
    }

    factory {
        GetLatestArrivalsUseCase(
            getDrinkPreviewUseCase = get(),
            repository = get()
        )
    }

    factory {
        UpdateLatestArrivalsUseCase(
            latestArrivalsRepository = get(),
            drinkPreviewRepository = get()
        )
    }

    factory {
        UpdateMostPopularUseCase(
            mostPopularRepository = get(),
            drinkPreviewRepository = get()
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
        FilterDrinkByIngredientUseCase(
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
            ingredientFilter = get<FilterDrinkByIngredientUseCase>(),
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
            getGlassesUseCase = get(),
            getCategoriesUseCase = get(),
            getIngredientsByNameUseCase = get()
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
            repository = get(),
            inAppReviewRepository = get()
        )
    }

    /*factory {
        GetWatchlistUseCase(
            watchlistRepository = get(),
            drinkPreviewRepository = get()
        )
    }*/

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

    /*factory {
        ToggleWatchlistUseCase(
            getWatchlistUseCase = get(),
            addToWatchlistUseCase = get(),
            removeFromWatchlistUseCase = get()
        )
    }*/
}
