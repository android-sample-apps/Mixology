package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.IngredientDetailsResponse
import com.yanivsos.mixological.repo.models.IngredientsWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.IngredientDetailsParam
import com.yanivsos.mixological.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.*
import java.util.function.Function

class IngredientDetailsRepository(
    private val service: DrinkService,
    private val reactiveStore: ReactiveStore<String, IngredientDetailsModel, IngredientDetailsParam>,
    private val mapper: Function<IngredientsWrapperResponse<IngredientDetailsResponse>, List<IngredientDetailsModel>>
) {

    fun getAll() = reactiveStore.getAll()

    fun getByName(name: String) : Flow<List<IngredientDetailsModel>> {
        return reactiveStore.getByParam(IngredientDetailsParam.GetByName(name.toLowerCase(Locale.ROOT)))
    }

    suspend fun fetchByName(ingredientName: String) {
        Timber.d("fetching ingredient[$ingredientName]")
        val response = service.getIngredientByName(ingredientName)
        val entity = mapper.apply(response)
        reactiveStore.storeAll(entity)
        Timber.d("fetched ingredient[$entity]")
    }

}