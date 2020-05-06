package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetCategoriesUseCase
import com.zemingo.drinksmenu.domain.GetDrinkPreviewByCategoryUseCase
import com.zemingo.drinksmenu.domain.models.CategoryModel
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.CategoryUiModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class CategoriesViewModel(
    categoriesUseCase: GetCategoriesUseCase,
    private val getDrinkPreviewByCategoryUseCase: GetDrinkPreviewByCategoryUseCase,
    categoriesMapper: Function<List<CategoryModel>, List<CategoryUiModel>>,
    previewMapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val categories: LiveData<List<CategoryUiModel>> =
        categoriesUseCase
            .categories
            .map { categoriesMapper.apply(it) }
            .map { it.toMutableList().apply {
                for (i in 0 .. 10) {
                    add(CategoryUiModel("$i"))
                }
            } }
            .asLiveData()

    val drinkPreviews: LiveData<List<DrinkPreviewUiModel>> =
        getDrinkPreviewByCategoryUseCase
            .drinkPreviews
            .map {
                previewMapper.apply(it)
            }.asLiveData()


    fun updateCategory(category: String) {
        getDrinkPreviewByCategoryUseCase
            .get(category)
    }
}