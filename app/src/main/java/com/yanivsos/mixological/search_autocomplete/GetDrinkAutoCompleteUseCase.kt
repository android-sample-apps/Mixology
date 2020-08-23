package com.yanivsos.mixological.search_autocomplete

import com.yanivsos.mixological.domain.GetDrinkPreviewUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetDrinkAutoCompleteUseCase(
    getDrinkPreviewUseCase: GetDrinkPreviewUseCase,
    private val mapper: DrinkListAutoCompleteMapper
) {
    val suggestions: Flow<List<DrinkAutoCompleteModel>> = getDrinkPreviewUseCase
        .getAll()
        .distinctUntilChanged()
        .map { mapper.apply(it) }
        .distinctUntilChanged()
}

