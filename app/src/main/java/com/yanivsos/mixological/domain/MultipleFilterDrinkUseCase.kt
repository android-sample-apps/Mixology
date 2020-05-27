package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.FilterType
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MultipleFilterDrinkUseCase(
    private val drinkPreviewRepository: DrinkPreviewRepository,
    private val combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    private val getDrinkPreviewUseCase: GetDrinkPreviewUseCase,
    private val alcoholicFilter: Filterable,
    private val categoryFilter: Filterable,
    private val ingredientFilter: Filterable,
    private val glassFilter: Filterable,
    private val nameFilter: Filterable
) {
    private val debounceTime = 250L

    private var resultsJob: Job? = null
    private var selectedFiltersJob: Job? = null
    private val _filerResultChannel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    private val _filerSelectedResultChannel =
        ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    val filterResults = _filerResultChannel.asFlow()

    val selectedFilters = _filerSelectedResultChannel.asFlow()

    init {
        resultsJob = observeFilterResults()
        selectedFiltersJob = observeFilterSelectedResults()
    }

    private fun observeFilterResults(): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            val filterResults = alcoholicFilter.results
                .combineFilters(categoryFilter.results, "alcoholic")
                .combineFilters(ingredientFilter.results, "ingredients")
                .combineFilters(glassFilter.results, "glasses")
                .combineFilters(nameFilter.results, "by names")
                .onEach { storeIfNotNull(it) }
                .combine(getDrinkPreviewUseCase.getAll()) { filters: List<DrinkPreviewModel>?, storedDrinks: List<DrinkPreviewModel> ->
                    if (filters != null) {
                        Timber.d("received ${filters.size} filters")
                        filters
                    } else {
                        Timber.d("received inactive filters, ${storedDrinks.size} from storage")
                        storedDrinks
                    }
                }
                .filterNotNull()
                .distinctUntilChanged()
                .debounce(debounceTime)


            combineWithFavoriteUseCase
                .combine(filterResults)
                .collect {
                    Timber.i("Received ${it.size} items")
                    _filerResultChannel.send(it)
                }
        }
    }

    private fun storeIfNotNull(previews: List<DrinkPreviewModel>?) {
        if (previews == null) return
        GlobalScope.launch(Dispatchers.IO) {
            drinkPreviewRepository.storeAll(previews)
        }
    }

    private fun observeFilterSelectedResults() = GlobalScope.launch(Dispatchers.IO) {
        _filerSelectedResultChannel.send(emptyMap())
        alcoholicFilter
            .selectedFilters
            .combineSelectedFilters(categoryFilter.selectedFilters)
            .combineSelectedFilters(ingredientFilter.selectedFilters)
            .combineSelectedFilters(glassFilter.selectedFilters)
            .debounce(debounceTime)
            .distinctUntilChanged()
            .collect {
                Timber.d("selectedFilters: $it")
                _filerSelectedResultChannel.send(it)
            }
    }

    private fun Flow<List<DrinkPreviewModel>?>.combineFilters(
        new: Flow<List<DrinkPreviewModel>?>,
        tag: String
    ): Flow<List<DrinkPreviewModel>?> {
        return combine(new) { previous: List<DrinkPreviewModel>?, current: List<DrinkPreviewModel>? ->
            Timber.d("received ${previous?.size ?: "inactive"} $tag")
            combineFilters(previous, current)
        }
    }

    private fun Flow<Map<FilterType, Set<String>>>.combineSelectedFilters(new: Flow<Map<FilterType, Set<String>>>): Flow<Map<FilterType, Set<String>>> {
        return combine(new) { previousFilters, newFilter ->
            mergeSelectedFilters(previousFilters, newFilter)
        }
    }

    private fun mergeSelectedFilters(
        previous: Map<FilterType, Set<String>>,
        current: Map<FilterType, Set<String>>
    ): Map<FilterType, Set<String>> {
        return (previous.asSequence() + current.asSequence())
            .distinct()
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) ->
                val keySet = mutableSetOf<String>()
                values.forEach {
                    keySet.addAll(it)
                }
                keySet
            }
    }

    private fun combineFilters(
        previous: List<DrinkPreviewModel>?,
        current: List<DrinkPreviewModel>?
    ): List<DrinkPreviewModel>? {
        return when {
            previous == null && current == null -> null
            previous == null -> current
            current == null -> previous
            else -> previous
                .intersect(current)
                .toList()
        }
    }

    fun updateFilter(drinkFilter: DrinkFilter) {
        Timber.d("updating filter $drinkFilter")
        drinkFilter.run {
            when (type) {
                FilterType.ALCOHOL -> alcoholicFilter.filter(this)
                FilterType.CATEGORY -> categoryFilter.filter(this)
                FilterType.GLASS -> glassFilter.filter(this)
                FilterType.INGREDIENTS -> ingredientFilter.filter(this)
                FilterType.NAME -> nameFilter.filter(this)
            }
        }
    }

    fun cancel() {
        Timber.d("cancelled")
        alcoholicFilter.clear()
        categoryFilter.clear()
        ingredientFilter.clear()
        glassFilter.clear()
        nameFilter.clear()
        selectedFiltersJob?.cancel()
        resultsJob?.cancel()
    }
}