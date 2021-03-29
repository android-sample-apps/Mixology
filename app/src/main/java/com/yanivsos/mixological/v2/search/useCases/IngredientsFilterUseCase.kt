package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.drink.repo.DrinkFilterRequest
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

class IngredientsFilterUseCase(
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FilterUseCase<DrinkFilter.Ingredients> {

    private val mutex = Mutex()
    private val resultsFlow = MutableStateFlow<AccumulativeFilterState>(AccumulativeFilterState.All)

    override val results: Flow<AccumulativeFilterState> = resultsFlow.onEach {
        Timber.d("filterState: $it")
    }

    override suspend fun toggle(filter: DrinkFilter.Ingredients) {
        internalToggle(filter)
    }

    override suspend fun clear() {
        Timber.d("clear: ")
        clearResults()
    }

    private suspend fun clearResults() {
        withContext(defaultDispatcher) {
            mutex.withLock { emit() }
        }
    }

    private fun emit() {
        resultsFlow.value = AccumulativeFilterState.All
    }

    private suspend fun internalToggle(filter: DrinkFilter.Ingredients) {
        withContext(defaultDispatcher) {
            mutex.withLock {
                val filterState = resultsFlow.value
                Timber.d("currentState: $filterState")
                when (filterState) {
                    AccumulativeFilterState.All -> fetchIngredients(emptySet(), filter)
                    is AccumulativeFilterState.Result -> fetchIngredients(
                        filterState.filters.filterIsInstance<DrinkFilter.Ingredients>().toSet(),
                        filter
                    )
                }
            }
        }
    }

    private suspend fun fetchIngredients(
        ingredients: Set<DrinkFilter.Ingredients>,
        filter: DrinkFilter.Ingredients
    ) {
        val drinkFilter = DrinkFilter.Ingredients(filter.ingredient)
        if (drinkFilter in ingredients) {
            ingredients.toMutableSet().apply { remove(drinkFilter) }
        } else {
            ingredients.toMutableSet().apply { add(drinkFilter) }
        }.also { newIngredients ->
            Timber.d("fetching by ingredients[$newIngredients]")
            val results =
                drinkRepository.filterBy(DrinkFilterRequest.Ingredients(newIngredients.map { it.ingredient }))
            Timber.d("fetch result: $results")
            emit(newIngredients, results)
        }
    }

    private suspend fun emit(
        ingredients: Set<DrinkFilter.Ingredients>,
        results: List<DrinkPreviewModel>
    ) {
        withContext(defaultDispatcher) {
            AccumulativeFilterState.Result(
                filters = ingredients,
                results = results.toSet()
            )
        }.also {
            resultsFlow.value = it
        }
    }
}
