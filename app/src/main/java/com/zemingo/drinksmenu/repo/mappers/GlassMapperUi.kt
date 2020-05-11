package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
import com.zemingo.drinksmenu.ui.models.GlassUiModel
import java.util.function.Function

class GlassMapperUi : Function<List<GlassModel>, List<GlassUiModel>> {

    override fun apply(t: List<GlassModel>): List<GlassUiModel> {
        return t.map {
            GlassUiModel(
                name = it.name
            )
        }
    }
}