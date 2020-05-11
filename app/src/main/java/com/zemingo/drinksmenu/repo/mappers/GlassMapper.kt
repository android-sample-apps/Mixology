package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
import java.util.function.Function

class GlassMapper : Function<DrinksWrapperResponse<GlassResponse>, List<GlassModel>> {

    override fun apply(t: DrinksWrapperResponse<GlassResponse>): List<GlassModel> {
        return t
            .data
            .mapNotNull { it.strGlass }
            .filter { it.isNotEmpty() }
            .map {
                GlassModel(
                    name = it
                )
            }
    }
}