package com.yanivsos.mixological.v2.ingredients.useCases

import com.yanivsos.mixological.v2.ingredients.repository.IngredientDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FetchAndStoreIngredientUseCase(
    private val ingredientDetailsRepository: IngredientDetailsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun fetchAndStore(vararg names: String) {
        withContext(defaultDispatcher) {
            names.forEach { name ->
                fetchAndStore(name)
            }
        }
    }

    private suspend fun fetchAndStore(name: String) {
        runCatching {
            Timber.d("fetching: $name")
            ingredientDetailsRepository.fetchIngredient(name)
        }.onSuccess {
            Timber.d("fetched: $name")
            ingredientDetailsRepository.store(it)
        }
            .onFailure { Timber.e(it, "Failed fetching ingredient [$name]") }
    }
}
