package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.CategoryModel
import com.zemingo.drinksmenu.repo.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        repository.fetch()
    }
}