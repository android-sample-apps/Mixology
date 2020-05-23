package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.Result
import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class GetDrinkUseCase(
    private val repository: DrinkRepository,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    private val drinkId: String
) {

    private var job: Job? = null

    private val channel = ConflatedBroadcastChannel<Result<DrinkModel>>()
    val drinkChannel = channel.asFlow().distinctUntilChanged()

    init { observeDrink() }

    private fun observeDrink() {
        job?.cancel()
        job = GlobalScope.launch(Dispatchers.IO) {
            repository
                .get(drinkId)
                .onStart { channel.send(Result.Loading(drinkId)) }
                .onEach { delay(8000) }
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

    fun refresh() {
        observeDrink()
    }

    fun cancel() {
        Timber.d("cancel called")
        job?.cancel()
    }
}