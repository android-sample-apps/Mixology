package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.repo.repositories.AlcoholicFilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class GetAlcoholicFiltersUseCase(
    private val repository: AlcoholicFilterRepository
) {
    private val _channel = ConflatedBroadcastChannel<List<AlcoholicFilterModel>>()
    val alcoholicFilters = _channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
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

    private suspend fun fetch() {
        try {
            repository.fetchAll()
        } catch (e: Exception) {
            Timber.e(e, "Failed fetching alcoholic filters")
        }
    }
}