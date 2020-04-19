package com.zemingo.cocktailmenu.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.cocktailmenu.domain.GetCategoriesUseCase
import com.zemingo.cocktailmenu.models.CategoryEntity
import com.zemingo.cocktailmenu.models.CategoryUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class CategoriesViewModel(
    categoriesUseCase: GetCategoriesUseCase,
    mapper: Function<List<CategoryEntity>, List<CategoryUiModel>>
) : ViewModel() {

    val categories: LiveData<List<CategoryUiModel>> =
        categoriesUseCase
            .categories
            .map { mapper.apply(it) }
            .asLiveData()
}