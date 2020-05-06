package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.repo.mappers.*
import com.zemingo.drinksmenu.ui.mappers.CategoryMapperUi
import com.zemingo.drinksmenu.ui.mappers.DrinkMapperUi
import com.zemingo.drinksmenu.ui.mappers.DrinkPreviewMapperUi
import com.zemingo.drinksmenu.ui.mappers.IngredientDetailsMapperUi
import org.koin.dsl.module

val mappersModule = module {

    factory { DrinkPreviewMapperUi() }

    factory { DrinkPreviewMapper() }

    factory { CategoryMapperUi() }

    factory { CategoryMapper() }

    factory { IngredientMapper() }

    factory { DrinkMapper() }

    factory { DrinkMapperUi() }

    factory { IngredientDetailsMapper() }

    factory { IngredientDetailsMapperUi() }

}