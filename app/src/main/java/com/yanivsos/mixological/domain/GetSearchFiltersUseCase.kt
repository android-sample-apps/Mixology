package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

class GetSearchFiltersUseCase(
    getAlcoholicFiltersUseCase: GetAlcoholicFiltersUseCase,
    getGlassesUseCase: GetGlassesUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val getIngredientsByNameUseCase: GetIngredientsByNameUseCase
) {

    private val _channel = ConflatedBroadcastChannel<SearchFiltersModel>()
    val results = _channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            getAlcoholicFiltersUseCase.alcoholicFilters
                .combine(getGlassesUseCase.glasses) { alc: List<AlcoholicFilterModel>, gls: List<GlassModel> ->
                    Timber.d("received: [${alc.size}] alcoholic, [${gls.size}] glasses")
                    SearchFiltersModel(
                        alcoholic = alc.sortedBy { it.name },
                        glasses = gls.sortedBy { it.name }
                    )
                }
                .combine(getCategoriesUseCase.categories) { model: SearchFiltersModel, ctgr: List<CategoryModel> ->
                    Timber.d("received: [${ctgr.size}] categories")
                    model.copy(categories = ctgr.sortedBy { it.name })
                }
                .combine(getIngredientsByNameUseCase.ingredients) { model: SearchFiltersModel, ingredients: List<IngredientModel> ->
                    Timber.d("received: [${ingredients.size}] ingredients")
                    model.copy(ingredients = ingredients.sortedBy { it.name })
                }
                .distinctUntilChanged()
                .collect {
                    _channel.send(it)
                }
        }
    }

    fun filterIngredientName(name: String?) {
        getIngredientsByNameUseCase.updateName(name ?: "")
    }

    fun clear() {
        getIngredientsByNameUseCase.clear()
    }
}