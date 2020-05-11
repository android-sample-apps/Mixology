package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.ui.models.AlcoholFilterUiModel
import java.util.function.Function

class AlcoholicFilterMapperUi : Function<List<AlcoholicFilterModel>, List<AlcoholFilterUiModel>> {

    override fun apply(t: List<AlcoholicFilterModel>): List<AlcoholFilterUiModel> {
        return t.map { AlcoholFilterUiModel(name = it.name) }
    }
}