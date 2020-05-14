package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MultipleFilterDrinkUseCase(
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
            alcoholicFilter.results
                .combine(categoryFilter.results) { alcoholic: List<DrinkPreviewModel>?,
                                                   category: List<DrinkPreviewModel>? ->
                    Timber.d("received ${alcoholic?.size ?: "inactive"} alcoholic")
                    combineFilters(alcoholic, category)
                }
                .combine(ingredientFilter.results) { previous: List<DrinkPreviewModel>?,
                                                     ingredients: List<DrinkPreviewModel>? ->
                    Timber.d("received ${ingredients?.size ?: "inactive"} ingredients")
                    combineFilters(previous, ingredients)
                }
                .combine(glassFilter.results) { previous: List<DrinkPreviewModel>?,
                                                glass: List<DrinkPreviewModel>? ->
                    Timber.d("received ${glass?.size ?: "inactive"} glass")
                    combineFilters(previous, glass)
                }
                .combine(nameFilter.results) { previous: List<DrinkPreviewModel>?, byName: List<DrinkPreviewModel>? ->
                    Timber.d("received ${byName?.size ?: "inactive"} results by name")
                    combineFilters(previous, byName)
                }
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
                .collect {
                    Timber.i("Received ${it.size} items")
                    _filerResultChannel.send(it)
                }
        }
    }

    private fun observeFilterSelectedResults() = GlobalScope.launch(Dispatchers.IO) {
        _filerSelectedResultChannel.send(emptyMap())
        alcoholicFilter
            .selectedFilters
            .combine(categoryFilter.selectedFilters) { alcoholicFilters, categoryFilters ->
                Timber.d("selectedFilters: $alcoholicFilters")
                combineSelectedFilters(alcoholicFilters, categoryFilters)
            }
            .combine(ingredientFilter.selectedFilters) { previousFilters, ingredientsFilters ->
                combineSelectedFilters(previousFilters, ingredientsFilters)
            }
            .combine(glassFilter.selectedFilters) { previousFilters, glassFilter ->
                combineSelectedFilters(previousFilters, glassFilter)
            }
            .combine(nameFilter.selectedFilters) { previousFilters, nameFilter ->
                combineSelectedFilters(previousFilters, nameFilter)
            }
            .debounce(debounceTime)
            .distinctUntilChanged()
            .collect {
                Timber.d("selectedFilters: $it")
                _filerSelectedResultChannel.send(it)
            }
    }


    private fun combineSelectedFilters(
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