package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.drink.repo.DrinkFilterRequest
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class AccumulativeFilterByUseCase<T : DrinkFilter>(
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FilterUseCase<T> {
    private val mutex = Mutex()
    private val resultFlow =
        MutableStateFlow<AccumulativeFilterState>(AccumulativeFilterState.All)
    override val results: Flow<AccumulativeFilterState> = resultFlow

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

    private suspend fun toggleFilter(filter: T) {
        withContext(defaultDispatcher) {
            mutex.withLock {
                if (resultMap.containsKey(filter)) {
                    Timber.d("toggleFilter: removing $filter")
                    resultMap.remove(filter)
                    emitMap(resultMap)
                } else {
                    runCatching {
                        Timber.d("toggleFilter: fetching $filter ...")
                        drinkRepository.filterBy(toFilterRequest(filter))
                    }.onSuccess {
                        Timber.d("toggleFilter: fetched $filter")
                        resultMap[filter] = it
                        emitMap(resultMap)
                    }.onFailure { Timber.e(it, "failed to fetch filter $filter") }
                }
            }
        }
    }

    private suspend fun clearResults() {
        withContext(defaultDispatcher) {
            mutex.withLock {
                resultMap.clear()
                emitMap(resultMap)
            }
        }
    }

    private suspend fun emitMap(map: Map<DrinkFilter, List<DrinkPreviewModel>>) {
        withContext(defaultDispatcher) {
            resultFlow.value = if (map.isEmpty()) {
                Timber.d("emitting empty")
                AccumulativeFilterState.All
            } else {
                Timber.d("emitting map: keys[${map.keys}]")
                AccumulativeFilterState.Result(
                    filters = map.keys,
                    results = map.values.flatten().toSet()
                )
            }
        }
    }
}

sealed class AccumulativeFilterState {
    object All : AccumulativeFilterState()
    data class Result(
        val filters: Set<DrinkFilter>,
        val results: Set<DrinkPreviewModel>
    ) : AccumulativeFilterState()
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
