package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class GetDrinkPreviewByCategoryUseCase(
    private val repo: DrinkPreviewRepository,
    category: String
) {
    private val channel = Channel<List<DrinkPreviewModel>>()
    val drinkPreviews = channel.consumeAsFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            fetchByCategory(category)
            repo
                .getAll()
                .collect { channel.send(it) }
        }
    }

    private suspend fun fetchByCategory(category: String) {
        repo.fetchByCategory(category)
    }
}