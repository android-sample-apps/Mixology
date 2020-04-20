package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.domain.GetDrinkPreviewByCategoryUseCase
import com.zemingo.drinksmenu.repo.mappers.DrinkPreviewMapper
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import com.zemingo.drinksmenu.view_model.CategoriesViewModel
import com.zemingo.drinksmenu.view_model.DrinkPreviewByCategoryViewModel
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
}