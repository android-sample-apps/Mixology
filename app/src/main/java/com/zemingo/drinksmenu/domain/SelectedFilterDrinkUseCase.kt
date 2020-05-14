package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class SelectedFilterDrinkUseCase {

    private var job: Job? = null
    private val _selectedFiltersChannel = ConflatedBroadcastChannel<Map<FilterType, Set<String>>>()
    val selectedFilters = _selectedFiltersChannel.asFlow()

    private val cache = Collections.synchronizedMap(mutableMapOf<FilterType, MutableSet<String>>())

    init {
        GlobalScope.launch(Dispatchers.IO) {
            send()
        }
    }

    fun updateFilter(drinkFilter: DrinkFilter) {
        job = GlobalScope.launch(Dispatchers.IO) {
            when {
                !drinkFilter.active -> removeFilter(drinkFilter)
                else -> addFilter(drinkFilter)
            }
            Timber.d("Sending: $cache")
            send()
        }
    }

    private fun changeFilter(drinkFilter: DrinkFilter) {
        cache[drinkFilter.type] = mutableSetOf(drinkFilter.query)
    }

    private fun removeFilter(drinkFilter: DrinkFilter) {
        val filterSet = cache[drinkFilter.type] ?: return
        filterSet.remove(drinkFilter.query)
    }

    private fun appendFilter(drinkFilter: DrinkFilter) {
        cache[drinkFilter.type]?.add(drinkFilter.query) ?: run {
            cache[drinkFilter.type] = mutableSetOf(drinkFilter.query)
        }
    }

    private fun addFilter(drinkFilter: DrinkFilter) {
        if (multipleSelectionEnabled(drinkFilter.type)) {
            appendFilter(drinkFilter)
        } else {
            changeFilter(drinkFilter)
        }
    }

    fun cancel() {
        Timber.d("cancelled")
        job?.cancel()
    }

    private suspend fun send() {
        _selectedFiltersChannel.send(cache)
    }

    private fun multipleSelectionEnabled(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.INGREDIENTS -> true
            else -> false
        }
    }
}