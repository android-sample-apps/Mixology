package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class GetDrinkPreviewByCategoryUseCase(
    private val repository: DrinkPreviewRepository,
    category: String
) {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val drinkPreviews = channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            channel.send(fetchByCategory(category))
        }
    }

    private suspend fun fetchByCategory(category: String): List<DrinkPreviewModel> {
        return repository.fetchByCategoryImmediate(category)
    }
}