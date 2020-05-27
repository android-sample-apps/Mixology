package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.domain.CombineWithFavoriteUseCase
import com.yanivsos.mixological.domain.GetCategoriesUseCase
import com.yanivsos.mixological.domain.GetDrinkPreviewByCategoryUseCase
import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.function.Function

class CategoriesViewModel(
    combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    categoriesUseCase: GetCategoriesUseCase,
    private val getDrinkPreviewByCategoryUseCase: GetDrinkPreviewByCategoryUseCase,
    categoriesMapper: Function<List<CategoryModel>, List<CategoryUiModel>>,
    previewMapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    private var categoryJob: Job? = null

    val categories: LiveData<List<CategoryUiModel>> =
        categoriesUseCase
            .categories
            .map { categoriesMapper.apply(it) }
            /*.map { it.toMutableList().apply {
                for (i in 0 .. 10) {
                    add(CategoryUiModel("$i"))
                }
            } }*/
            .asLiveData()

    val drinkPreviews: LiveData<List<DrinkPreviewUiModel>> =
        combineWithFavoriteUseCase.combine(getDrinkPreviewByCategoryUseCase
            .drinkPreviews)
            .map { previewMapper.apply(it) }
            .asLiveData()


    fun updateCategory(category: String) {
        categoryJob?.cancel()
        categoryJob = viewModelScope.launch(Dispatchers.IO) {
            getDrinkPreviewByCategoryUseCase
                .get(category)
        }
    }

    override fun onCleared() {
        super.onCleared()
        categoryJob?.cancel()
    }
}