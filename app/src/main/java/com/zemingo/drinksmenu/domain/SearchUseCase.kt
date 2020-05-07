package com.zemingo.drinksmenu.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

abstract class SearchUseCase<T> {

    private val channel = ConflatedBroadcastChannel<List<T>>()
    val searchResults = channel.asFlow()

    private var searchJob: Job? = null

    protected abstract suspend fun fetchQuery(query: String): List<T>

    fun search(query: String) {
        Timber.d("Searching by [$query]")
        clearOngoingSearch()
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            val result: List<T> = try {
                fetchQuery(query)
            } catch (e: CancellationException) {
                Timber.i(e, "Searching by [$query] cancelled")
                emptyList()
            } catch (e: Exception) {
                Timber.e(e, "Failed searching by [$query], sending an empty list")
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