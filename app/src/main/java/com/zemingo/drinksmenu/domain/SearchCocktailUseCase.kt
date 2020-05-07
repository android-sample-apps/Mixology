package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

class SearchCocktailUseCase(
    private val repository: AdvancedSearchRepository
) {

    private val channel = ConflatedBroadcastChannel<List<DrinkModel>>()
    val searchChannel = channel.asFlow()

    private var searchJob: Job? = null

    fun searchByName(name: String) {
        Timber.d("Searching by name[$name]")
        clearOngoingSearch()
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            val drinks: List<DrinkModel> = try {
                repository.fetchByName(name)
            } catch (e: CancellationException) {
                Timber.i(e, "Searching by name cancelled")
                emptyList()
            } catch (e: Exception) {
                Timber.e(e, "Failed searching by name[$name], sending an empty list")
                emptyList()
            }

            channel.send(drinks)
        }
    }

    fun clearOngoingSearch() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }
}