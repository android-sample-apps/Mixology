package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.domain.models.Result
import com.yanivsos.mixological.repo.repositories.DrinkRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class GetDrinkUseCase(
    private val repository: DrinkRepository,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val drinkId: String
) {

    private var job: Job? = null

    private val channel = ConflatedBroadcastChannel<Result<DrinkModel>>()
    val drinkChannel = channel.asFlow().distinctUntilChanged()

    init {
        observeDrink()
    }

    private fun observeDrink() {
        job?.cancel()
        job = GlobalScope.launch(Dispatchers.IO) {
            repository
                .get(drinkId).combine(
                    getWatchlistUseCase.getById(drinkId).map { it != null }
                ) { drink, isFavorite ->
                    drink to isFavorite
                }
                .onStart { channel.send(Result.Loading(drinkId)) }
//                .onEach { delay(3000) }
                .collect { (drink, isFavorite) ->
                    if (drink == null) {
                        Timber.d("couldn't find drink[$drinkId] in DB - fetching...")
                        fetchAndStore()
                    } else {
                        Timber.d("found drink[$drinkId]")
                        val model = drink.copy(isFavorite = isFavorite)
                        channel.send(Result.Success(model))
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