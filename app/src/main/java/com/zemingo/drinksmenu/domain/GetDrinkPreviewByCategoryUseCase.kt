package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

class GetDrinkPreviewByCategoryUseCase(
    private val repository: DrinkPreviewRepository
) {
    private val channel = ConflatedBroadcastChannel<List<DrinkPreviewModel>>()
    val drinkPreviews = channel.asFlow()

    suspend fun get(category: String) {
        val drinksInCategory = try {
            fetchByCategory(category)

        } catch (e: Exception) {
            Timber.e(e, "Failed getting drinks")
            null
        }
        drinksInCategory?.let { channel.send(it) }
    }

    private suspend fun fetchByCategory(category: String): List<DrinkPreviewModel> {
        return repository.fetchByCategoryImmediate(category)
    }
}