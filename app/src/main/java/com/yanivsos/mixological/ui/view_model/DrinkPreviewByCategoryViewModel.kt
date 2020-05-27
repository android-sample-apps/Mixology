package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yanivsos.mixological.domain.GetDrinkPreviewByCategoryUseCase
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class DrinkPreviewByCategoryViewModel(
    getDrinkPreviewByCategoryUseCase: GetDrinkPreviewByCategoryUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val drinkPreviews: LiveData<List<DrinkPreviewUiModel>> =
        getDrinkPreviewByCategoryUseCase
            .drinkPreviews
            .map { mapper.apply(it) }
            .asLiveData()
}