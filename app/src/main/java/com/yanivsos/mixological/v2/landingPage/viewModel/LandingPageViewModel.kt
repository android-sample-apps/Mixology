package com.yanivsos.mixological.v2.landingPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.ui.models.LandingPageUiModel
import com.yanivsos.mixological.v2.landingPage.toUiModel
import com.yanivsos.mixological.v2.landingPage.useCases.GetLandingPagePreviewsUseCase
import com.yanivsos.mixological.v2.landingPage.useCases.LandingPageModel
import com.yanivsos.mixological.v2.landingPage.useCases.RefreshLandingPagePreviewsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LandingPageViewModel(
    getLandingPagePreviewsUseCase: GetLandingPagePreviewsUseCase,
    private val refreshLandingPagePreviewsUseCase: RefreshLandingPagePreviewsUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    init {
        Timber.d("init: hashcode:${hashCode()}")
        refreshLandingPage()
    }

    private fun refreshLandingPage() {
        viewModelScope.launch {
            refreshLandingPagePreviewsUseCase.refresh()
        }
    }

    val landingPageState: Flow<LandingPageState> =
        getLandingPagePreviewsUseCase
            .landingPagePreviews
            .map { mapToState(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LandingPageState.Loading)


    private suspend fun mapToState(landingPageModel: LandingPageModel): LandingPageState {
        return withContext(defaultDispatcher) {
            LandingPageState.Success(landingPageModel.toUiModel())
        }
    }
}

sealed class LandingPageState {
    object Loading : LandingPageState()
    data class Success(val landingPageUiModel: LandingPageUiModel) : LandingPageState()
}
