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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
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
    private val categorySelectedMutableFlow = MutableStateFlow("")
    val categorySelected: Flow<String> = categorySelectedMutableFlow

    val categories: LiveData<List<CategoryUiModel>> =
        categoriesUseCase
            .categories
            .map { categoriesMapper.apply(it) }
            .flowOn(Dispatchers.IO)
            .asLiveData()

    val drinkPreviews: LiveData<List<DrinkPreviewUiModel>> =
        combineWithFavoriteUseCase.combine(
            getDrinkPreviewByCategoryUseCase
                .drinkPreviews
        )
            .map { previewMapper.apply(it) }
            .flowOn(Dispatchers.IO)
            .asLiveData()


    fun updateCategory(category: String) {
        categoryJob?.cancel()
        categorySelectedMutableFlow.value = category
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