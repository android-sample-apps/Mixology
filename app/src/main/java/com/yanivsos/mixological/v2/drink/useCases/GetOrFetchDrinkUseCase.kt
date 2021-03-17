package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.domain.models.DrinkModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GetOrFetchDrinkUseCase(
    getDrinkUseCase: GetDrinkUseCase,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : CoroutineScope {

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + mainDispatcher + CoroutineName("GetOrFetchDrinkUseCase")

    private val flow = MutableStateFlow<GetOrFetchDrinkResult>(GetOrFetchDrinkResult.Loading)
    val drinkFlow = flow

    init {
        Timber.d("hashcode: ${hashCode()}")
        getDrinkUseCase
            .drink
            .onEach { onResult(it) }
            .launchIn(this)
    }

    private suspend fun onResult(result: DrinkModelResult) {
        Timber.d("onResult: $result")
        when (result) {
            is DrinkModelResult.Found -> onDrinkFound(result.drinkModel)
            is DrinkModelResult.NotFount -> onDrinkNotFount(result.drinkId)
        }
    }

    private suspend fun onDrinkNotFount(drinkId: String) {
        Timber.d("fetchAndStore: called with id[$drinkId]")
        runCatching {
            fetchAndStoreDrinkUseCase
                .fetchAndStore(drinkId)
        }.onSuccess { Timber.d("fetched & stored drink[$drinkId]") }
            .onFailure { emit(GetOrFetchDrinkResult.Error(it, drinkId)) }
    }

    private suspend fun onDrinkFound(drinkModel: DrinkModel) {
        emit(GetOrFetchDrinkResult.Success(drinkModel))
        refreshQuietly(drinkModel.id)
    }

    private suspend fun refreshQuietly(drinkId: String) {
        kotlin.runCatching {
            fetchAndStoreDrinkUseCase.fetchAndStore(drinkId)
        }.onFailure { Timber.w(it, "refresh failed") }
    }

    private fun emit(result: GetOrFetchDrinkResult) {
        flow.value = result
    }
}

sealed class GetOrFetchDrinkResult {
    object Loading : GetOrFetchDrinkResult()
    data class Success(val drinkModel: DrinkModel) : GetOrFetchDrinkResult()
    data class Error(val throwable: Throwable, val drinkId: String) : GetOrFetchDrinkResult()
}
