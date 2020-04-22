package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetPreviousSearchResultsUseCase(
    private val repository: DrinkPreviewRepository
) {
    fun previousSearches(): Flow<List<DrinkPreviewModel>> {
        //todo - replace with search history items
        return repository.getAll()
    }
}