package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import java.util.*

interface Filterable {
    val results: Flow<List<DrinkPreviewModel>?>
    val selectedFilters: Flow<Map<FilterType, Set<String>>>
    fun filter(drinkFilter: DrinkFilter)
    fun clear()
}

class FilterDrinkUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) : Filterable {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>?>()
    private val selectedFilterChannel = ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    override val results = channel.asFlow()

    override val selectedFilters: Flow<Map<FilterType, Set<String>>> = selectedFilterChannel.asFlow()

    private var searchJob: Job? = null

    init {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            sendNotActive()
        }
    }

    override fun filter(drinkFilter: DrinkFilter) {
        Timber.d("filtering by [$drinkFilter]")
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            if (!drinkFilter.active) {
                sendNotActive()
            } else {
                sendFilterQuery(drinkFilter)
            }
        }
    }

    private suspend fun sendFilterQuery(filter: DrinkFilter) {
        val result: List<DrinkPreviewModel> = try {
            fetchQuery(filter)
        } catch (e: CancellationException) {
            Timber.i(e, "filtering by [$filter] cancelled")
            emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Failed searching by [$filter], sending an empty list")
            emptyList()
        }

        selectedFilterChannel.send(filter.toSelectedFilter())
        channel.send(result)
    }

    private suspend fun sendNotActive() {
        selectedFilterChannel.send(emptyMap())
        channel.send(null)
    }

    private fun DrinkFilter.toSelectedFilter(): Map<FilterType, Set<String>> {
        return mutableMapOf<FilterType, Set<String>>().apply {
            put(type, setOf(query))
        }
    }

    override fun clear() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return advancedSearchRepository.filterBy(filter)
    }
}

abstract class AggregateFilterDrinkUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) : Filterable {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>?>()
    override val results = channel.asFlow()

    private val selectedFilterChannel = ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    override val selectedFilters: Flow<Map<FilterType, Set<String>>> = selectedFilterChannel.asFlow()

    private val cache =
        Collections.synchronizedMap(mutableMapOf<DrinkFilter, List<DrinkPreviewModel>?>())

    private val selectedFiltersCache =
        Collections.synchronizedMap(mutableMapOf<FilterType, MutableSet<String>>())

    private var searchJob: Job? = null

    init {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            sendNotActive()
        }
    }

    override fun filter(drinkFilter: DrinkFilter) {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            if (cache.containsKey(drinkFilter)) {
                Timber.d("filtering by existing [$drinkFilter]")
            } else {
                Timber.d("filtering by new [$drinkFilter]")
            }

            internalFilter(drinkFilter)
        }
    }

    private suspend fun internalFilter(filter: DrinkFilter) {
        if (!filter.active) {
            removeFromCache(filter)
        } else {
            sendFilterQuery(filter)
                ?.let { addToCache(filter, it) }
        }
        sendFromCache()
    }

    private suspend fun sendFilterQuery(filter: DrinkFilter): List<DrinkPreviewModel>? {
        return try {
            fetchQuery(filter)
        } catch (e: CancellationException) {
            Timber.i(e, "filtering by [$filter] cancelled")
            null
        } catch (e: Exception) {
            Timber.e(e, "Failed searching by [$filter], sending an empty list")
            null
        }
    }

    private fun addToCache(filter: DrinkFilter, result: List<DrinkPreviewModel>?) {
        selectedFiltersCache[filter.type]?.add(filter.query) ?: run {
            selectedFiltersCache[filter.type] = mutableSetOf(filter.query)
        }
        cache[filter] = result
    }

    private fun removeFromCache(filter: DrinkFilter) {
        selectedFiltersCache[filter.type]?.remove(filter.query)
        cache.remove(filter)
    }

    private suspend fun sendFromCache() {
        if (cache.isEmpty()) {
            Timber.d("empty cache: sending null")
            sendNotActive()
            return
        }
        val elements = cache.asSequence()
            .mapNotNull { it.value }
            .combineFilters()
            ?.distinctBy { it.id }

        Timber.d("sendFromCache: results[$elements], cache[$cache]")
        selectedFilterChannel.send(selectedFiltersCache)
        channel.send(elements)
    }

    protected abstract suspend fun Sequence<List<DrinkPreviewModel>>.combineFilters(): List<DrinkPreviewModel>?

    private suspend fun sendNotActive() {
        selectedFilterChannel.send(selectedFiltersCache)
        channel.send(null)
    }

    override fun clear() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return advancedSearchRepository.filterBy(filter)
    }
}

class UnionFilterDrinkUseCase(advancedSearchRepository: AdvancedSearchRepository) :
    AggregateFilterDrinkUseCase(
        advancedSearchRepository
    ) {
    override suspend fun Sequence<List<DrinkPreviewModel>>.combineFilters(): List<DrinkPreviewModel> {
        val returnValue = mutableListOf<DrinkPreviewModel>()
        forEach { returnValue.addAll(it) }
        return returnValue
    }
}

class IntersectionFilterDrinkUseCase(advancedSearchRepository: AdvancedSearchRepository) :
    AggregateFilterDrinkUseCase(
        advancedSearchRepository
    ) {

    override suspend fun Sequence<List<DrinkPreviewModel>>.combineFilters(): List<DrinkPreviewModel> {
        var intersection: MutableList<DrinkPreviewModel>? = null
        forEach { list ->
            intersection?.let {
                intersection = it.intersect(list).toMutableList()
            } ?: run {
                intersection = list.toMutableList()
            }
        }

        return intersection ?: emptyList()
    }
}