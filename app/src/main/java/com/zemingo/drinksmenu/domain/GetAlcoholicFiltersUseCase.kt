package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.repositories.AlcoholicFilterRepository
import com.zemingo.drinksmenu.repo.repositories.GlassRepository
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