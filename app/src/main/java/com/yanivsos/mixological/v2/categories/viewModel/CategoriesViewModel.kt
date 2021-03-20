package com.yanivsos.mixological.v2.categories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.SelectedCategoryUiModel
import com.yanivsos.mixological.v2.categories.useCases.CategoriesState
import com.yanivsos.mixological.v2.categories.useCases.GetCategoriesStateUseCase
import com.yanivsos.mixological.v2.mappers.toUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CategoriesViewModel(
    private val getCategoriesStateUseCase: GetCategoriesStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    var previewsExpanded = false

    val categoriesState: Flow<CategoriesUiState> =
        getCategoriesStateUseCase
            .categories
            .map { mapToUiState(it) }

    init {
        Timber.d("init: hashcode: ${hashCode()}")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }

    private suspend fun mapToUiState(categoriesState: CategoriesState): CategoriesUiState {
        return withContext(defaultDispatcher) {
            categoriesState.toUiState()
        }
    }

    fun updateSelected(categoryUiModel: CategoryUiModel) {
        viewModelScope.launch {
            getCategoriesStateUseCase.updateSelected(categoryUiModel)
        }
    }
}

sealed class CategoriesUiState {
    object Loading : CategoriesUiState()

    data class CategoryNotSelected(
        val categories: List<CategoryUiModel>
    ) : CategoriesUiState()

    data class CategorySelected(
        val categories: List<CategoryUiModel>,
        val selectedCategoryUiModel: SelectedCategoryUiModel
    ) : CategoriesUiState()
}

