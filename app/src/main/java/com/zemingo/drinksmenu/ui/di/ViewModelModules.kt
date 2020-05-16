package com.zemingo.drinksmenu.ui.di

import com.zemingo.drinksmenu.ui.mappers.*
import com.zemingo.drinksmenu.ui.view_model.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CategoriesViewModel(
            categoriesUseCase = get(),
            getDrinkPreviewByCategoryUseCase = get(),
            categoriesMapper = get<CategoryMapperUi>(),
            previewMapper = get<DrinkPreviewMapperUi>()
        )
    }

    viewModel {
        DrinkPreviewByCategoryViewModel(
            getDrinkPreviewByCategoryUseCase = get(),
            mapper = get<DrinkPreviewMapperUi>()
        )
    }

    viewModel { (id: String) ->
        DrinkViewModel(
            getDrinkUseCase = get { parametersOf(id) },
            mapper = get<DrinkMapperUi>()
        )
    }

    viewModel {
        SearchViewModel(
            getDrinkPreviewUseCase = get(),
            getPreviousSearchResultsUseCase = get(),
            markAsSearchedDrinkPreviewUseCase = get(),
            mapper = get<DrinkPreviewMapperUi>()
        )
    }

    viewModel {
        LandingPageViewModel(
            mostPopularUseCase = get(),
            latestArrivalsUseCase = get(),
            recentSearchesUseCase = get(),
            mapper = get<DrinkPreviewMapperUi>()
        )
    }

    viewModel { (name: String) ->
        IngredientDetailsViewModel(
            getIngredientDetailsUseCase = get { parametersOf(name) },
            mapper = get<IngredientDetailsMapperUi>()
        )
    }

    viewModel {
        AdvancedSearchViewModel(
            getSearchFiltersUseCase = get(),
            filter = get(),
            mapper = get<DrinkPreviewMapperUi>(),
            searchMapper = get<SearchFiltersMapperUi>()
        )
    }

    viewModel { (id: String) ->
        DrinkPreviewOptionsViewModel(
            addToWatchlistUseCase = get(),
            getWatchlistUseCase = get(),
            id = id
        )
    }

    viewModel {
        WatchlistViewModel(
            getWatchlistUseCase = get(),
            mapper = get<DrinkPreviewMapperUi>()
        )
    }
}