package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.FilterType
import com.yanivsos.mixological.repo.repositories.AdvancedSearchRepository
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
    fun cancel()
}

class FilterDrinkUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) : Filterable {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>?>()
    private val selectedFilterChannel = ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    override val results = channel.asFlow()

    override val selectedFilters: Flow<Map<FilterType, Set<String>>> =
        selectedFilterChannel.asFlow()

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

        selectedFilterChannel.offer(filter.toSelectedFilter())
        channel.offer(result)
    }

    private fun sendNotActive() {
        selectedFilterChannel.offer(emptyMap())
        channel.offer(null)
    }

    private fun DrinkFilter.toSelectedFilter(): Map<FilterType, Set<String>> {
        return mutableMapOf<FilterType, Set<String>>().apply {
            put(type, setOf(query))
        }
    }

    override fun clear() {
        cancel()
        sendNotActive()
    }

    override fun cancel() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return advancedSearchRepository.filterBy(filter)
    }
}

class FilterDrinkByIngredientUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) : Filterable {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>?>()
    private val selectedFilterChannel = ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    override val results = channel.asFlow()
    override val selectedFilters: Flow<Map<FilterType, Set<String>>> =
        selectedFilterChannel.asFlow()

    private var searchJob: Job? = null

    private val filterCache = mutableSetOf<DrinkFilter>()

    init {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            sendNotActive()
        }
    }

    override fun filter(drinkFilter: DrinkFilter) {
        Timber.d("filtering by [$drinkFilter]")
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            if (!drinkFilter.active) {
                removeFromCache(drinkFilter)
                if (filterCache.isEmpty()) {
                    sendNotActive()
                } else {
                    sendFilterQuery()
                }
            } else {
                sendFilterQuery(drinkFilter) { addToCache(drinkFilter) }
            }
        }
    }

    private fun addToCache(drinkFilter: DrinkFilter) {
        Timber.d("adding to cache[$drinkFilter]")
        filterCache.add(drinkFilter)
    }

    private fun removeFromCache(drinkFilter: DrinkFilter) {
        Timber.d("removing from cache[$drinkFilter]")
        filterCache.remove(drinkFilter)
    }

    private suspend fun sendFilterQuery(
        filter: DrinkFilter? = null,
        onFinished: (() -> Unit)? = null
    ) {
        val result: List<DrinkPreviewModel> = try {
            val searchQuery = buildSearchQuery(filter)
            fetchQuery(searchQuery).apply {
                onFinished?.invoke()
            }
        } catch (e: CancellationException) {
            Timber.i(e, "filtering by [$filter] cancelled")
            emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Failed searching by [$filter], sending an empty list")
            emptyList()
        }

        selectedFilterChannel.send(buildSelectedFilters())
        channel.send(result)
    }

    private fun sendNotActive() {
        selectedFilterChannel.offer(inactiveFilters())
        channel.offer(null)
    }

    override fun clear() {
        cancel()
        filterCache.clear()
        sendNotActive()
    }

    private fun inactiveFilters() =
        mutableMapOf<FilterType, Set<String>>()
            .apply { put(FilterType.INGREDIENTS, emptySet()) }

    private fun buildSelectedFilters(): Map<FilterType, Set<String>> {
        val ingredientSet = filterCache.map { it.query }.toSet()
        return mutableMapOf<FilterType, Set<String>>().apply {
            put(FilterType.INGREDIENTS, ingredientSet)
            Timber.d("buildSelectedFilters: $this")
        }
    }

    override fun cancel() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private fun buildSearchQuery(filter: DrinkFilter? = null): DrinkFilter {
        val ingredientQuery = filterCache.toMutableSet().apply {
            filter?.let { add(filter) }
        }.map { it.query }.joinToString(separator = ",") { it.replace(" ", "_") }

        return DrinkFilter(
            query = ingredientQuery,
            type = FilterType.INGREDIENTS
        )
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
    override val selectedFilters: Flow<Map<FilterType, Set<String>>> =
        selectedFilterChannel.asFlow()

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

    override fun clear() {
        cancel()
        selectedFiltersCache.clear()
        sendNotActive()
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

    private fun sendNotActive() {
        selectedFilterChannel.offer(selectedFiltersCache)
        channel.offer(null)
    }

    override fun cancel() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return try {
            advancedSearchRepository.filterBy(filter)
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch query: $filter")
            emptyList()
        }
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