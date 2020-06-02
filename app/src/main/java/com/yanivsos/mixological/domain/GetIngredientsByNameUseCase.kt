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

class GetIngredientsByNameUseCase(
    private val repository: IngredientRepository
) {

    private val _channel = ConflatedBroadcastChannel<List<IngredientModel>>()
    val ingredients = _channel.asFlow()
    private var job: Job? = null

    init {
        updateName("")
    }

    fun updateName(name: String) {
        clear()
        job = GlobalScope.launch(Dispatchers.IO) {
            repository
                .getByName(name)
                .collect {
                    _channel.send(it)
                }
        }
    }

    fun clear() {
        job?.cancel()
    }
}