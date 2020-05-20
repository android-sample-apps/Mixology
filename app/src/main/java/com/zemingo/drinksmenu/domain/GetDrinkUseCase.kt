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
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
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
                        fetchAndStore()
                    } else {
                        Timber.d("found drink[$drinkId]")
                        channel.send(drink)
                    }
                }
        }
    }

    private suspend fun fetchAndStore() {
        Timber.d("fetchById: called with id[$drinkId]")
        try {
            fetchAndStoreDrinkUseCase.fetchAndStore(drinkId)
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch by id[$drinkId]")

        }
    }

    fun cancel() {
        Timber.d("cancel called")
        job?.cancel()
    }
}