package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.repo.repositories.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
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

    private var job: Job? = null

    fun refresh() {
        job = GlobalScope.launch(Dispatchers.IO) {
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

    fun cancel() {
        job?.cancel()
    }

    private suspend fun fetch() {
        try {
            repository.fetchIngredients()
        } catch (e: Exception) {
            Timber.d(e, "Failed to fetch ingredients")
        }
    }
}