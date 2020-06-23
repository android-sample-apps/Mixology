package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.domain.models.CategoryWithImageModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GetCategoriesAndImagesUseCase(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val repository: DrinkPreviewRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val channel = ConflatedBroadcastChannel<List<CategoryWithImageModel>>()
    val categories = channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            getCategoriesUseCase
                .categories
                .map { categories ->
                    val categoriesWithImages = categories.map {
                        val image = fetchImage(it)
                        CategoryWithImageModel(it, image)
                    }
                    categoriesWithImages
                }
                .collect { categories ->
                    channel.send(categories)
                }
        }
    }

    private suspend fun fetchImage(categoryModel: CategoryModel): String? {
        return try {
            repository.fetchByCategoryImmediate(categoryModel.name).lastOrNull()?.thumbnail
        } catch (e: Exception) {
            null
        }
    }

}