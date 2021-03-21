package com.yanivsos.mixological.v2.ingredients.di

import com.yanivsos.mixological.repo.room.DrinksDatabase
import com.yanivsos.mixological.v2.ingredients.IngredientDetailsViewModel
import com.yanivsos.mixological.v2.ingredients.repository.IngredientsRepository
import com.yanivsos.mixological.v2.ingredients.useCases.FetchAndStoreIngredientUseCase
import com.yanivsos.mixological.v2.ingredients.useCases.GetIngredientDetailsUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ingredientsDi = module {

    single { get<DrinksDatabase>().getIngredientDetailsDao() }

    single {
        IngredientsRepository(
            drinkService = get(),
            ingredientDetailsDao = get()
        )
    }

    single {
        FetchAndStoreIngredientUseCase(
            ingredientsRepository = get()
        )
    }

    single {
        GetIngredientDetailsUseCase(
            repository = get()
        )
    }

    viewModel { (ingredientName: String) ->
        IngredientDetailsViewModel(
            getIngredientUseCase = get(),
            ingredientName = ingredientName
        )
    }
}
