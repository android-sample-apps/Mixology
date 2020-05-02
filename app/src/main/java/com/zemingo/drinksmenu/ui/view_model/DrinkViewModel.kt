package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.*
import com.zemingo.drinksmenu.domain.GetDrinkUseCase
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.function.Function

class DrinkViewModel(
    getDrinkUseCase: GetDrinkUseCase,
    mapper: Function<DrinkModel, DrinkUiModel>
) : ViewModel() {

    val drink: LiveData<DrinkUiModel> =
        getDrinkUseCase
            .drink
            .map { mapper.apply(it) }
            .asLiveData()

}