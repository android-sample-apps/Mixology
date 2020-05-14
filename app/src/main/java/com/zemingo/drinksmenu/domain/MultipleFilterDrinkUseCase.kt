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
    private var resultsJob: Job? = null
    private val _filerResultChannel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val filterResults = _filerResultChannel.asFlow()

    private val selectedFiltersUseCase = SelectedFilterDrinkUseCase()
    val selectedFilters = selectedFiltersUseCase.selectedFilters

    init {
        resultsJob = GlobalScope.launch(Dispatchers.IO) {
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
                    }
                    else {
                        Timber.d("received inactive filters, ${storedDrinks.size} from storage")
                        storedDrinks
                    }
                }
                .filterNotNull()
                .distinctUntilChanged()
                .debounce(250L)
                .collect {
                    Timber.i("Received ${it.size} items")
                    _filerResultChannel.send(it)
                }
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

        selectedFiltersUseCase.updateFilter(drinkFilter)
    }

    fun cancel() {
        Timber.d("cancelled")
        alcoholicFilter.clear()
        categoryFilter.clear()
        ingredientFilter.clear()
        glassFilter.clear()
        nameFilter.clear()
        selectedFiltersUseCase.cancel()
        resultsJob?.cancel()
    }
}