package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.repo.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class GetCategoriesUseCase(
    private val repo: CategoryRepository
) {
    private val channel = Channel<List<CategoryModel>>()
    val categories = channel.consumeAsFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            repo
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
        repo.fetch()
    }
}