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
    private val alcoholicFilter: FilterDrinkUseCase,
    private val categoryFilter: FilterDrinkUseCase,
    private val ingredientFilter: FilterDrinkUseCase,
    private val glassFilter: FilterDrinkUseCase,
    private val nameFilter: FilterDrinkUseCase
) {
    private var resultsJob: Job? = null
    private var filterJob: Job? = null
    private val _filerResultChannel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val filterResults = _filerResultChannel.asFlow()

    private val _selectedFiltersChannel = ConflatedBroadcastChannel<Map<FilterType, String>>()
    val selectedFilters = _selectedFiltersChannel.asFlow()

    private val selectedFiltersCache = mutableMapOf<FilterType, String>()



    init {

        resultsJob = GlobalScope.launch(Dispatchers.IO) {
            _selectedFiltersChannel.send(emptyMap())
            alcoholicFilter.searchResults
                .combine(categoryFilter.searchResults) { alcoholic: List<DrinkPreviewModel>?,
                                                         category: List<DrinkPreviewModel>? ->
                    Timber.d("received ${alcoholic?.size ?: "inactive"} alcoholic")
                    combineFilters(alcoholic, category)
                }
                .combine(ingredientFilter.searchResults) { previous: List<DrinkPreviewModel>?,
                                                           ingredients: List<DrinkPreviewModel>? ->
                    Timber.d("received ${ingredients?.size ?: "inactive"} ingredients")
                    combineFilters(previous, ingredients)
                }
                .combine(glassFilter.searchResults) { previous: List<DrinkPreviewModel>?,
                                                      glass: List<DrinkPreviewModel>? ->
                    Timber.d("received ${glass?.size ?: "inactive"} glass")
                    combineFilters(previous, glass)
                }
                .combine(nameFilter.searchResults) { previous: List<DrinkPreviewModel>?, byName: List<DrinkPreviewModel>? ->
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

        setSelectedFilter(drinkFilter)
    }

    //todo - replace with something better
    private fun setSelectedFilter(drinkFilter: DrinkFilter) {
        filterJob = GlobalScope.launch(Dispatchers.IO) {
            drinkFilter.run {
                selectedFiltersCache[type]?.let { key ->
                    if (key != drinkFilter.query) {
                        selectedFiltersCache[type] = drinkFilter.query
                    } else {
                        selectedFiltersCache[type] = ""
                    }
                } ?: run {
                    selectedFiltersCache[type] = drinkFilter.query
                }

                _selectedFiltersChannel.send(selectedFiltersCache)
            }
        }
    }

    fun cancel() {
        Timber.d("cancelled")
        alcoholicFilter.clearOngoingSearch()
        categoryFilter.clearOngoingSearch()
        ingredientFilter.clearOngoingSearch()
        glassFilter.clearOngoingSearch()
        nameFilter.clearOngoingSearch()
        resultsJob?.cancel()
    }
}