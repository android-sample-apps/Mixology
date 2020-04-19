package com.zemingo.cocktailmenu.di

import com.zemingo.cocktailmenu.repo.mappers.CategoryMapper
import com.zemingo.cocktailmenu.repo.mappers.DrinkMapper
import com.zemingo.cocktailmenu.repo.mappers.DrinkPreviewMapper
import com.zemingo.cocktailmenu.repo.mappers.IngredientsMapper
import com.zemingo.cocktailmenu.view_model.mappers.CategoryMapperUi
import com.zemingo.cocktailmenu.view_model.mappers.DrinkMapperUi
import com.zemingo.cocktailmenu.view_model.mappers.DrinkPreviewMapperUi
import org.koin.dsl.module

val mappersModule = module {

    factory { DrinkMapperUi() }

    factory { DrinkPreviewMapperUi() }

    factory { DrinkMapper(IngredientsMapper()) }

    factory { DrinkPreviewMapper() }

    factory { CategoryMapperUi() }

    factory { CategoryMapper() }


}