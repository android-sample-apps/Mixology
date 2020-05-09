package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber

class AdvancedSearchUseCase(
    private val searchCocktailByIngredientUseCase: FilterDrinkUseCase,
    private val searchCocktailByNameUseCase: SearchUseCase<String, DrinkModel>
) {
    private var searchJob: Job? = null
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val searchResults = channel.asFlow()

    private val searchByName = searchCocktailByNameUseCase.searchResults
    private val searchByIngredient = searchCocktailByIngredientUseCase.searchResults

    private fun observerSearchResults() {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            searchByName
                .map { mapToPreviews(it) }
                .zip(searchByIngredient) { fromNames: List<DrinkPreviewModel>, containingIngredients: List<DrinkPreviewModel> ->
                    Timber.d("Collected fromNames[${fromNames.size}], containing ingredients[${containingIngredients.size}]")
                    mutableListOf<DrinkPreviewModel>().apply {
                        addAll(fromNames)
                        addAll(containingIngredients)
                    }
                }
                .map { results: List<DrinkPreviewModel> -> results.distinctBy { it.id } }
                .map { results: List<DrinkPreviewModel> -> results.sortedBy { it.name } }
                .collect {
                    Timber.d("Collected ${it.size} distinct items")
                    channel.send(it)
                }
        }
    }

    private fun mapToPreviews(drinks: List<DrinkModel>): List<DrinkPreviewModel> {
        return drinks.map { DrinkPreviewModel(it) }
    }

    fun search(query: String) {
        Timber.d("searching for query[$query]")
        clearOnGoingSearch()
        observerSearchResults()
        searchCocktailByIngredientUseCase.filter(DrinkFilter(query, FilterType.INGREDIENTS))
        searchCocktailByNameUseCase.filter(query)
    }

    fun clearOnGoingSearch() {
        stopNestedSearch()
        searchJob?.cancel()
    }

    private fun stopNestedSearch() {
        searchCocktailByIngredientUseCase.clearOngoingSearch()
        searchCocktailByNameUseCase.clearOngoingSearch()
    }

}
