package com.zemingo.drinksmenu.ui.di

import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import com.zemingo.drinksmenu.ui.view_model.*
import com.zemingo.drinksmenu.ui.view_model.mappers.CategoryMapperUi
import com.zemingo.drinksmenu.ui.view_model.mappers.DrinkPreviewMapperUi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CategoriesViewModel(
            categoriesUseCase = get(),
            mapper = get<CategoryMapperUi>()
        )
    }

    viewModel { (category: String) ->
        DrinkPreviewByCategoryViewModel(
            getDrinkPreviewByCategoryUseCase = get { parametersOf(category) },
            mapper = get<DrinkPreviewMapperUi>()
        )
    }

    viewModel {
        DrinkViewModel(
            drinkRepository = get<DrinkRepository>()
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
}