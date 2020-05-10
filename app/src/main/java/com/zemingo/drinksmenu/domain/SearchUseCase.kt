package com.zemingo.drinksmenu.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

abstract class SearchUseCase<Q, T> {

    private val channel = ConflatedBroadcastChannel<List<T>>()
    val searchResults = channel.asFlow()

    private var searchJob: Job? = null

    protected abstract suspend fun fetchQuery(filter: Q): List<T>

    /*init {
        GlobalScope.launch(Dispatchers.IO) {
//            channel.send(emptyList())
        }
    }*/

    fun filter(filter: Q) {
        Timber.d("filtering by [$filter]")
        clearOngoingSearch()
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            val result: List<T> = try {
                fetchQuery(filter)
            } catch (e: CancellationException) {
                Timber.i(e, "filtering by [$filter] cancelled")
                emptyList()
            } catch (e: Exception) {
                Timber.e(e, "Failed searching by [$filter], sending an empty list")
                emptyList()
            }

            channel.send(result)
        }
    }

    fun clearOngoingSearch() {
        Timber.d("Clearing ongoing searches")
        searchJob?.cancel()
    }
}