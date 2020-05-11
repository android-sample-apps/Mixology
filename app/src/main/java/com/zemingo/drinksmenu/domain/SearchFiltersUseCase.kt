package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SearchFiltersUseCase(
    getAlcoholicFiltersUseCase: GetAlcoholicFiltersUseCase,
    getGlassesUseCase: GetGlassesUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    getIngredientsUseCase: GetIngredientsUseCase
) {

    private val _channel = ConflatedBroadcastChannel<SearchFiltersModel>()
    val results = _channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            getAlcoholicFiltersUseCase.alcoholicFilters
                .combine(getGlassesUseCase.glasses) { alc: List<AlcoholicFilterModel>, gls: List<GlassModel> ->
                    SearchFiltersModel(
                        alcoholic = alc,
                        glasses = gls
                    )
                }
                .combine(getCategoriesUseCase.categories) { model: SearchFiltersModel, ctgr: List<CategoryModel> ->
                    model.copy(categories = ctgr)
                }
                .combine(getIngredientsUseCase.getAll()) { model: SearchFiltersModel, ingredients: List<IngredientModel> ->
                    model.copy(ingredients = ingredients)
                }
                .collect {
                    _channel.send(it)
                }
        }
    }
}