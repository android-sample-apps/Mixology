package com.zemingo.drinksmenu.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetCategoriesUseCase
import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.models.CategoryUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class CategoriesViewModel(
    categoriesUseCase: GetCategoriesUseCase,
    mapper: Function<List<CategoryModel>, List<CategoryUiModel>>
) : ViewModel() {

    val categories: LiveData<List<CategoryUiModel>> =
        categoriesUseCase
            .categories
            .map { mapper.apply(it) }
            .asLiveData()
}