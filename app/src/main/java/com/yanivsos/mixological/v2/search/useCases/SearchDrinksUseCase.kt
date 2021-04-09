package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchDrinksUseCase(
    drinkRepository: DrinkRepository,
    getAllFiltersUseCase: GetAllFiltersUseCase,
    private val alcoholicFilterByUseCase: AlcoholicFilterUseCase,
    private val glassFilterByUseCase: GlassFilterUseCase,
    private val categoriesFilterByUseCase: CategoryFilterUseCase,
    private val ingredientsFilterUseCase: IngredientsFilterUseCase,
    private val fetchDrinkByNameUseCase: FetchDrinkByNameUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val filterResults: Flow<AccumulativeFilterState> =
        alcoholicFilterByUseCase
            .results.combine(glassFilterByUseCase.results) { alcoholics, glasses ->
                mergeFilterStates(alcoholics, glasses)
            }.combine(categoriesFilterByUseCase.results) { previous, categories ->
                mergeFilterStates(previous, categories)
            }.combine(ingredientsFilterUseCase.results) { previous, ingredients ->
                mergeFilterStates(previous, ingredients)
            }


    val previews: Flow<List<DrinkPreviewModel>> =
        filterResults
            .combine(drinkRepository.getAllPreviews()) { filterResults, allPreviews ->
                mapToPreviews(filterResults, allPreviews)
            }.combine(fetchDrinkByNameUseCase.fetchDrinkState) { filteredResults, resultsByName ->
                intersectDrinks(filteredResults, resultsByName)
            }

    val filters: Flow<SelectedFilters> =
        filterResults.combine(getAllFiltersUseCase.allFilters) { filterResults, allFilters ->
            mapToFilterModel(filterResults, allFilters)
        }

    val filterOperators: Flow<FilterOperators> =
        alcoholicFilterByUseCase.operator
            .combine(ingredientsFilterUseCase.operator) { alcoholOperator, ingredientOperator ->
                FilterOperators(
                    ingredientOperator = ingredientOperator,
                    alcoholicOperator = alcoholOperator
                )
            }.combine(glassFilterByUseCase.operator) { operators, glassesOperator ->
                operators.copy(glassesOperator = glassesOperator)
            }.combine(categoriesFilterByUseCase.operator) { operators, categoriesOperator ->
                operators.copy(categoriesOperator = categoriesOperator)
            }

    suspend fun toggleFilter(drinkFilter: DrinkFilter) {
        withContext(defaultDispatcher) {
            when (drinkFilter) {
                is DrinkFilter.Alcoholic -> alcoholicFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Category -> categoriesFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Glass -> glassFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Ingredients -> ingredientsFilterUseCase.toggle(drinkFilter)
            }
        }
    }

    suspend fun clearAlcoholicFilter() {
        Timber.d("clearAlcoholicFilter")
        alcoholicFilterByUseCase.clear()
    }

    suspend fun clearGlassesFilter() {
        Timber.d("clearGlassesFilter")
        glassFilterByUseCase.clear()
    }

    suspend fun clearCategoriesFilter() {
        Timber.d("clearCategoriesFilter")
        categoriesFilterByUseCase.clear()
    }

    suspend fun clearIngredientsFilter() {
        Timber.d("clearIngredientsFilter")
        ingredientsFilterUseCase.clear()
    }

    suspend fun fetchByName(name: String) {
        Timber.d("fetchByName: name[$name]")
        fetchDrinkByNameUseCase.fetchByName(name)
    }

    fun clearByName() {
        Timber.d("clearByName: ")
        fetchDrinkByNameUseCase.clear()
    }

    fun setIngredientsOperator(operator: AccumulativeOperator) {
        ingredientsFilterUseCase.setOperator(operator)
    }

    private suspend fun mapToFilterModel(
        filter: AccumulativeFilterState,
        allFilters: SelectedFilters
    ): SelectedFilters {
        return withContext(defaultDispatcher) {
            when (filter) {
                AccumulativeFilterState.All -> allFilters
                is AccumulativeFilterState.Result -> combineToSelectedFilters(filter, allFilters)
            }
        }
    }

    private suspend fun intersectDrinks(
        filteredResults: List<DrinkPreviewModel>,
        drinkState: FetchDrinkState
    ): List<DrinkPreviewModel> {
        return withContext(defaultDispatcher) {
            when (drinkState) {
                is FetchDrinkState.FoundResults -> intersectDrinks(filteredResults, drinkState)
                FetchDrinkState.NoResults -> emptyList()
                FetchDrinkState.NotSelected -> filteredResults
            }
        }
    }

    private fun intersectDrinks(
        filteredResults: List<DrinkPreviewModel>,
        results: FetchDrinkState.FoundResults
    ): List<DrinkPreviewModel> {
        return filteredResults.intersectDrinks(results.drinks)
    }

    private fun combineToSelectedFilters(
        filter: AccumulativeFilterState.Result,
        allFilters: SelectedFilters
    ): SelectedFilters {

        val alcoholicSet: Set<String> =
            filter.toStringSet(DrinkFilter.Alcoholic::class.java) { it.alcoholic }
        val glassesSet: Set<String> =
            filter.toStringSet(DrinkFilter.Glass::class.java) { it.glass }
        val categoriesSet: Set<String> =
            filter.toStringSet(DrinkFilter.Category::class.java) { it.category }
        val ingredientsSet: Set<String> =
            filter.toStringSet(DrinkFilter.Ingredients::class.java) { it.ingredient }

        return SelectedFilters(
            glasses = allFilters.glasses.mergeSelected(glassesSet),
            alcoholic = allFilters.alcoholic.mergeSelected(alcoholicSet),
            categories = allFilters.categories.mergeSelected(categoriesSet),
            ingredients = allFilters.ingredients.mergeSelected(ingredientsSet)
        )
    }

    private inline fun <reified T : DrinkFilter> AccumulativeFilterState.Result.toStringSet(
        klass: Class<T>,
        mapBy: (T) -> String
    ) = filters.filterIsInstance(klass).map(mapBy).toSet()


    private fun FilterCollection.mergeSelected(selectedSet: Set<String>): FilterCollection {
        return FilterCollection(
            filters.map { it.copy(isSelected = it.name in selectedSet) },
            selectedSet.size
        )
    }

    private suspend fun mapToPreviews(
        filterResults: AccumulativeFilterState,
        allPreviews: List<DrinkPreviewModel>
    ): List<DrinkPreviewModel> {
        return withContext(defaultDispatcher) {
            when (filterResults) {
                AccumulativeFilterState.All -> allPreviews
                is AccumulativeFilterState.Result -> filterResults.results.toList()
                    .sortedBy { it.name }
            }
        }
    }

    private suspend fun mergeFilterStates(
        lhs: AccumulativeFilterState,
        rhs: AccumulativeFilterState
    ): AccumulativeFilterState {
        return withContext(defaultDispatcher) {
            when {
                (lhs is AccumulativeFilterState.All) && (rhs is AccumulativeFilterState.All) -> AccumulativeFilterState.All
                (lhs is AccumulativeFilterState.All) && (rhs is AccumulativeFilterState.Result) -> AccumulativeFilterState.Result(
                    filters = rhs.filters,
                    results = rhs.results
                )
                (lhs is AccumulativeFilterState.Result) && (rhs is AccumulativeFilterState.All) -> AccumulativeFilterState.Result(
                    filters = lhs.filters,
                    results = lhs.results
                )
                (lhs is AccumulativeFilterState.Result) && (rhs is AccumulativeFilterState.Result) -> AccumulativeFilterState.Result(
                    filters = lhs.filters + rhs.filters,
                    results = lhs.results.intersect(rhs.results)
                )
                else -> throw IllegalStateException("cannot find suitable case")
            }.also {
                Timber.d("mergeFilterStates: result[$it]")
            }
        }
    }
}


private fun List<DrinkPreviewModel>.intersectDrinks(list: List<DrinkPreviewModel>): List<DrinkPreviewModel> {
    val filteredResultsMap: Map<String, DrinkPreviewModel> =
        associateBy { it.id }
    return list.mapNotNull { filteredResultsMap[it.id] }
}

