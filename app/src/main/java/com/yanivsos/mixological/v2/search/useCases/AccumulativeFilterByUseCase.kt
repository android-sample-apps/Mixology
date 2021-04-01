package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.drink.repo.DrinkFilterRequest
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class AccumulativeFilterByUseCase<T : DrinkFilter>(
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    initialOperator: AccumulativeOperator = AccumulativeOperator.Intersection
) : FilterUseCase<T> {
    private val mutex = Mutex()
    private val operatorFlow = MutableStateFlow(initialOperator)
    private val resultFlow =
        MutableStateFlow<PreOperatorFilterResults>(PreOperatorFilterResults.All)

    override val results: Flow<AccumulativeFilterState> =
        resultFlow.combine(operatorFlow) { results, operator ->
            accumulateWithOperator(results, operator)
        }

    private val resultMap = mutableMapOf<DrinkFilter, List<DrinkPreviewModel>>()

    protected abstract fun toFilterRequest(filter: T): DrinkFilterRequest

    override suspend fun toggle(filter: T) {
        Timber.d("toggling filer: $filter")
        toggleFilter(filter)
    }

    override suspend fun clear() {
        Timber.d("clear")
        clearResults()
    }

    fun setOperator(operator: AccumulativeOperator) {
        Timber.d("setOperator: $operator")
        operatorFlow.value = operator
    }

    private suspend fun toggleFilter(filter: T) {
        withContext(defaultDispatcher) {
            mutex.withLock {
                if (resultMap.containsKey(filter)) {
                    Timber.d("toggleFilter: removing $filter")
                    resultMap.remove(filter)
                } else {
                    Timber.d("toggleFilter: fetching $filter ...")
                    resultMap[filter] = drinkRepository.filterBy(toFilterRequest(filter)).also {
                        Timber.d("toggleFilter: fetched $filter")
                    }
                }
                emitMap(resultMap.toMap())
            }
        }
    }

    private suspend fun clearResults() {
        withContext(defaultDispatcher) {
            mutex.withLock {
                resultMap.clear()
                emitMap(resultMap.toMap())
            }
        }
    }

    private suspend fun emitMap(map: Map<DrinkFilter, List<DrinkPreviewModel>>) {
        withContext(defaultDispatcher) {
            resultFlow.value = if (map.isEmpty()) {
                Timber.d("emitting empty")
                PreOperatorFilterResults.All
            } else {
                PreOperatorFilterResults.Result(
                    map = map
                )
            }
        }
    }

    private suspend fun accumulateWithOperator(
        results: PreOperatorFilterResults,
        operator: AccumulativeOperator
    ): AccumulativeFilterState = withContext(defaultDispatcher) {
        Timber.d("accumulateWithOperator: $operator, $results")
        when (results) {
            PreOperatorFilterResults.All -> AccumulativeFilterState.All
            is PreOperatorFilterResults.Result -> AccumulativeFilterState.Result(
                filters = results.map.keys,
                results = operator.accumulate(results.map)
            )
        }
    }
}


sealed class AccumulativeOperator {
    abstract fun accumulate(map: Map<DrinkFilter, List<DrinkPreviewModel>>): Set<DrinkPreviewModel>

    object Union : AccumulativeOperator() {
        override fun accumulate(map: Map<DrinkFilter, List<DrinkPreviewModel>>): Set<DrinkPreviewModel> {
            return map.values.flatten().toSet()
        }
    }

    object Intersection : AccumulativeOperator() {
        override fun accumulate(map: Map<DrinkFilter, List<DrinkPreviewModel>>): Set<DrinkPreviewModel> {
            var result = map.values.firstOrNull()?.toSet() ?: return emptySet()
            map.values
                .map { list -> list.toSet() }
                .forEach { set ->
                    result = result.intersect(set)
                }

            return result
        }
    }
}

private sealed class PreOperatorFilterResults {
    object All : PreOperatorFilterResults()
    data class Result(val map: Map<DrinkFilter, List<DrinkPreviewModel>>) :
        PreOperatorFilterResults()
}

sealed class AccumulativeFilterState {
    object All : AccumulativeFilterState()
    data class Result(
        val filters: Set<DrinkFilter>,
        val results: Set<DrinkPreviewModel>
    ) : AccumulativeFilterState()
}

class IngredientsFilterUseCase(
    drinkRepository: DrinkRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AccumulativeFilterByUseCase<DrinkFilter.Ingredients>(drinkRepository, defaultDispatcher) {

    override fun toFilterRequest(filter: DrinkFilter.Ingredients): DrinkFilterRequest {
        return DrinkFilterRequest.Ingredient(filter.ingredient)
    }
}

class AlcoholicFilterUseCase(
    drinkRepository: DrinkRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AccumulativeFilterByUseCase<DrinkFilter.Alcoholic>(drinkRepository, defaultDispatcher) {

    override fun toFilterRequest(filter: DrinkFilter.Alcoholic): DrinkFilterRequest {
        return DrinkFilterRequest.Alcoholic(filter.alcoholic)
    }
}

class CategoryFilterUseCase(
    drinkRepository: DrinkRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AccumulativeFilterByUseCase<DrinkFilter.Category>(drinkRepository, defaultDispatcher) {

    override fun toFilterRequest(filter: DrinkFilter.Category): DrinkFilterRequest {
        return DrinkFilterRequest.Category(filter.category)
    }
}

class GlassFilterUseCase(
    drinkRepository: DrinkRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AccumulativeFilterByUseCase<DrinkFilter.Glass>(drinkRepository, defaultDispatcher) {

    override fun toFilterRequest(filter: DrinkFilter.Glass): DrinkFilterRequest {
        return DrinkFilterRequest.Glass(filter.glass)
    }
}
