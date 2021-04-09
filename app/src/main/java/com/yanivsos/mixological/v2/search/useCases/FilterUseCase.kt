package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import kotlinx.coroutines.flow.Flow

interface FilterUseCase<T: DrinkFilter> {
    val results: Flow<AccumulativeFilterState>
    val operator: Flow<AccumulativeOperator>
    suspend fun toggle(filter: T)
    suspend fun clear()
}
