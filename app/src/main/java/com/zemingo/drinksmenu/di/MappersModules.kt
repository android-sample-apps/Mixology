package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.repo.mappers.CategoryMapper
import com.zemingo.drinksmenu.repo.mappers.DrinkMapper
import com.zemingo.drinksmenu.repo.mappers.DrinkPreviewMapper
import com.zemingo.drinksmenu.repo.mappers.IngredientMapper
import com.zemingo.drinksmenu.view_model.mappers.CategoryMapperUi
import com.zemingo.drinksmenu.view_model.mappers.DrinkPreviewMapperUi
import org.koin.dsl.module

val mappersModule = module {

    factory { DrinkPreviewMapperUi() }

    factory { DrinkPreviewMapper() }

    factory { CategoryMapperUi() }

    factory { CategoryMapper() }

    factory { IngredientMapper() }

    factory { DrinkMapper() }

}