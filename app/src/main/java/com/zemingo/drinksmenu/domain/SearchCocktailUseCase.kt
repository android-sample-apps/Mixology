package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchCocktailUseCase(
    private val repository: AdvancedSearchRepository
) {

    private val channel = ConflatedBroadcastChannel<List<DrinkModel>>()
    val searchChannel = channel.asFlow()

    fun searchByName(name: String) {
        Timber.d("Searching by name[$name]")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val drinks = repository.fetchByName(name)
                channel.send(drinks)
            } catch (e: Exception) {
                Timber.e(e, "Failed searching ny name[$name]")
                channel.send(emptyList())
            }
        }
    }
}