package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
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
    private val alcoholicFilterByUseCase: AccumulativeFilterByUseCase,
    private val glassFilterByUseCase: AccumulativeFilterByUseCase,
    private val categoriesFilterByUseCase: AccumulativeFilterByUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val filterResults: Flow<AccumulativeFilterState> =
        alcoholicFilterByUseCase
            .results.combine(glassFilterByUseCase.results) { alcoholics, glasses ->
                mergeFilterStates(alcoholics, glasses)
            }.combine(categoriesFilterByUseCase.results) { previous, categories ->
                mergeFilterStates(previous, categories)
            }


    val previews: Flow<List<DrinkPreviewModel>> =
        filterResults
            .combine(drinkRepository.getAllPreviews()) { filterResults, allPreviews ->
                mapToPreviews(filterResults, allPreviews)
            }

    val filters: Flow<SelectedFilters> =
        filterResults.combine(getAllFiltersUseCase.allFilters) { filterResults, allFilters ->
            mapToFilterModel(filterResults, allFilters)
        }

    suspend fun toggleFilter(drinkFilter: DrinkFilter) {
        withContext(defaultDispatcher) {
            when (drinkFilter) {
                is DrinkFilter.Alcoholic -> alcoholicFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Category -> categoriesFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Glass -> glassFilterByUseCase.toggle(drinkFilter)
                is DrinkFilter.Ingredients -> {
                    // TODO: 23/03/2021 implement ingredients
                    Timber.d("filter $drinkFilter unimplemented ")
                }
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
        // TODO: 23/03/2021 implement
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

    private fun combineToSelectedFilters(
        filter: AccumulativeFilterState.Result,
        allFilters: SelectedFilters
    ): SelectedFilters {

        val alcoholicSet: Set<String> =
            filter.filters.filterIsInstance<DrinkFilter.Alcoholic>().map { it.alcoholic }.toSet()
        val glassesSet: Set<String> =
            filter.filters.filterIsInstance<DrinkFilter.Glass>().map { it.glass }.toSet()
        val categoriesSet: Set<String> =
            filter.filters.filterIsInstance<DrinkFilter.Category>().map { it.category }.toSet()

        return SelectedFilters(
            glasses = allFilters.glasses.map { it.copy(isSelected = it.name in glassesSet) },
            alcoholic = allFilters.alcoholic.map { it.copy(isSelected = it.name in alcoholicSet) },
            categories = allFilters.categories.map { it.copy(isSelected = it.name in categoriesSet) },
            ingredients = emptyList() // TODO: 23/03/2021 complete ingredients
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
            }
        }
    }
}
