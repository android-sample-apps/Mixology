package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val channel = ConflatedBroadcastChannel<List<CategoryModel>>()
    val categories = channel.asFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .get()
                .collect { categories ->
                    if (categories.isEmpty()) {
                        fetchCategories()
                    }
                    channel.send(categories)
                }
        }
    }

    private suspend fun fetchCategories() {
        try {
            repository.fetch()
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch categories")
        }
    }
}