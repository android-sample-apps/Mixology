package com.yanivsos.mixological.v2.categories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.SelectedCategoryUiModel
import com.yanivsos.mixological.v2.categories.useCases.CategoriesState
import com.yanivsos.mixological.v2.categories.useCases.GetCategoriesStateUseCase
import com.yanivsos.mixological.v2.mappers.toUiState
import com.yanivsos.mixological.v2.search.useCases.FetchAndStoreCategoriesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class CategoriesViewModel(
    private val getCategoriesStateUseCase: GetCategoriesStateUseCase,
    private val fetchAndStoreCategoriesUseCase: FetchAndStoreCategoriesUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    var isExpanded: Boolean = false
        set(value) {
            onExpanded(value)
            field = value
        }

    private val isExpandedMutableFlow = MutableStateFlow(isExpanded)
    val isExpandedFlow: Flow<Boolean> = isExpandedMutableFlow
        .onEach { Timber.d("expand changed: isExpanded[$it] ") }

    val categoriesState: Flow<CategoriesUiState> =
        getCategoriesStateUseCase
            .categories
            .map { mapToUiState(it) }

    init {
        Timber.d("init: hashcode: ${hashCode()}")
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            runCatching {
                fetchAndStoreCategoriesUseCase.fetchAndStore()
            }.onFailure {
                Timber.w(it, "fetchCategories: Failed getting categories list")
            }.onSuccess { Timber.d("fetchCategories: categories list refreshed") }
        }
    }

    private fun onExpanded(isExpanded: Boolean) {
        Timber.d("onExpanded: $isExpanded")
        isExpandedMutableFlow.value = isExpanded
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

