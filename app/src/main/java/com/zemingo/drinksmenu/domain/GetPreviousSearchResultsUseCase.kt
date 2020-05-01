package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.SearchDrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GetPreviousSearchResultsUseCase(
    searchRepository: SearchDrinkPreviewRepository
) {

    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val previousSearches = channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            searchRepository
                .getHistory()
                .collect {
                    channel.send(it)
                }
        }
    }
}