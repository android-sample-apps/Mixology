package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetDrinkPreviewByCategoryUseCase
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
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