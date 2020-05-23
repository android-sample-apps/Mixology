package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.IngredientModel
import com.zemingo.drinksmenu.repo.repositories.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class GetIngredientsUseCase(
    private val repository: IngredientRepository
) {

    private val _channel = ConflatedBroadcastChannel<List<IngredientModel>>()
    val ingredients = _channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .getAll()
                .collect {
                    if (it.isEmpty()) {
                        fetch()
                    } else {
                        _channel.send(it)
                    }
                }
        }
    }

    private suspend fun fetch() {
        try {
            repository.fetchIngredients()
        } catch (e: Exception) {
            Timber.d(e, "Failed to fetch ingredients")
        }
    }
}