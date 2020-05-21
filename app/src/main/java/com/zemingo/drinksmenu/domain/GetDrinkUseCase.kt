package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.Result
import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class GetDrinkUseCase(
    private val repository: DrinkRepository,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    private val drinkId: String
) {

    private var job: Job? = null

    private val channel = ConflatedBroadcastChannel<Result<DrinkModel>>()
    val drinkChannel = channel.asFlow().distinctUntilChanged()

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
                        channel.send(Result.Success(drink))
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
            channel.send(Result.Error(e))
        }
    }

    fun cancel() {
        Timber.d("cancel called")
        job?.cancel()
    }
}