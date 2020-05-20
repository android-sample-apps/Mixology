package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class GetDrinkUseCase(
    private val repository: DrinkRepository,
    private val drinkId: String
) {

    private var job: Job? = null

    private val channel = ConflatedBroadcastChannel<DrinkModel>()
    val drinkChannel = channel.asFlow()

    init {
        job = GlobalScope.launch(Dispatchers.IO) {
            repository
                .get(drinkId)
                .collect { drink ->
                    if (drink == null) {
                        Timber.d("couldn't find drink[$drinkId] in DB - fetching...")
                        fetchById()
                    } else {
                        Timber.d("found drink[$drinkId]")
                        channel.send(drink)
                    }
                }
        }
    }

    private suspend fun fetchById() {
        Timber.d("fetchById: called with id[$drinkId]")
        try {
            val drinkModel = repository.fetch(drinkId)
            repository.store(drinkModel)
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch by id[$drinkId]")

        }
    }

    fun cancel() {
        Timber.d("cancel called")
        job?.cancel()
    }
}