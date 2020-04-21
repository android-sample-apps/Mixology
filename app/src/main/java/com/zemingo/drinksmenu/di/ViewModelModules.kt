package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import com.zemingo.drinksmenu.view_model.CategoriesViewModel
import com.zemingo.drinksmenu.view_model.DrinkPreviewByCategoryViewModel
import com.zemingo.drinksmenu.view_model.DrinkViewModel
import com.zemingo.drinksmenu.view_model.mappers.CategoryMapperUi
import com.zemingo.drinksmenu.view_model.mappers.DrinkPreviewMapperUi
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
}