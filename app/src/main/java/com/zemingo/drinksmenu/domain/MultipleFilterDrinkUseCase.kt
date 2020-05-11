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
    private val alcoholicFilter: FilterDrinkUseCase,
    private val categoryFilter: FilterDrinkUseCase,
    private val ingredientFilter: FilterDrinkUseCase,
    private val glassFilter: FilterDrinkUseCase

) {

    private var job: Job? = null
    private val _channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val filterResults = _channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            _channel.send(emptyList())
            alcoholicFilter.searchResults
                .combine(categoryFilter.searchResults) { alcoholic: List<DrinkPreviewModel>?,
                                                         category: List<DrinkPreviewModel>? ->
                    alcoholic?.let {
                        Timber.d("received ${it.size} alcoholic")
                    }
                    combineFilters(alcoholic, category)
                }
                .combine(ingredientFilter.searchResults) { previous: List<DrinkPreviewModel>?,
                                                           ingredients: List<DrinkPreviewModel>? ->
                    ingredients?.let {
                        Timber.d("received ${it.size} ingredients")
                    }
                    combineFilters(previous, ingredients)
                }
                .combine(glassFilter.searchResults) { previous: List<DrinkPreviewModel>?,
                                                      glass: List<DrinkPreviewModel>? ->
                    glass?.let {
                        Timber.d("received ${it.size} glass")
                    }
                    combineFilters(previous, glass)
                }
                .filterNotNull()
                .distinctUntilChanged()
                .debounce(250L)
                .collect {
                    Timber.i("Received ${it?.size} items")
                    _channel.send(it)
                }
        }
    }

    private fun observeFilter() {
        /*job = */
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
//        observeFilter()
        drinkFilter.run {
            when (type) {
                FilterType.ALCOHOL -> alcoholicFilter.filter(this)
                FilterType.CATEGORY -> categoryFilter.filter(this)
                FilterType.GLASS -> glassFilter.filter(this)
                FilterType.INGREDIENTS -> ingredientFilter.filter(this)
            }
        }
    }

    fun cancel() {
        Timber.d("cancelled")
//        job?.cancel()
    }
}