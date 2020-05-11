package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
import com.zemingo.drinksmenu.ui.models.AlcoholFilterUiModel
import com.zemingo.drinksmenu.ui.models.GlassUiModel
import java.util.function.Function

class AlcoholicFilterMapperUi : Function<List<AlcoholicFilterModel>, List<AlcoholFilterUiModel>> {

    override fun apply(t: List<AlcoholicFilterModel>): List<AlcoholFilterUiModel> {
        return t.map { AlcoholFilterUiModel(name = it.name) }
    }
}