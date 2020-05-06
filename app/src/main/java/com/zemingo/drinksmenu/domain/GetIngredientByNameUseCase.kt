package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.IngredientDetailsModel
import com.zemingo.drinksmenu.repo.repositories.IngredientDetailsRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class GetIngredientDetailsUseCase(
    private val repository: IngredientDetailsRepository,
    val ingredientName: String
) {
    private val channel = ConflatedBroadcastChannel<IngredientDetailsModel>()
    val ingredientDetails = channel.asFlow()

    suspend fun launch() {
        repository
            .getByName(ingredientName)
            .collect {
                if (it.isEmpty()) {
                    Timber.d("Repo empty for name[$ingredientName]. Fetching ...")
                    repository.fetchByName(ingredientName)
                } else {
                    val ingredient = it.first()
                    Timber.d("Collected ingredient[${ingredient.debugPrint()}]")
                    channel.send(ingredient)
                }
            }
    }
}