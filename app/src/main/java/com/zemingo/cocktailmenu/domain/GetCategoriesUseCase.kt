package com.zemingo.cocktailmenu.domain

import com.zemingo.cocktailmenu.models.CategoryEntity
import com.zemingo.cocktailmenu.repo.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GetCategoriesUseCase(
    private val repo: CategoryRepository
) {
    private val channel = Channel<List<CategoryEntity>>()
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