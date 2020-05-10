package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

class FilterDrinkUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) /*: SearchUseCase<DrinkFilter, DrinkPreviewModel>() */ {


    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>?>()
    val searchResults = channel.asFlow()

    private var searchJob: Job? = null

    init {
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            channel.send(null)
        }
    }

    fun filter(filter: DrinkFilter) {
        Timber.d("filtering by [$filter]")
//        clearOngoingSearch()
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            val result: List<DrinkPreviewModel> = try {
                fetchQuery(filter)
            } catch (e: CancellationException) {
                Timber.i(e, "filtering by [$filter] cancelled")
                emptyList()
            } catch (e: Exception) {
                Timber.e(e, "Failed searching by [$filter], sending an empty list")
                emptyList()
            }

            channel.send(result)
        }
    }

    fun clearOngoingSearch() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }

    private suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return advancedSearchRepository.filterBy(filter)
    }

}