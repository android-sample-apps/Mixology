package com.yanivsos.mixological.repo.mappers

import com.yanivsos.mixological.domain.models.GlassModel
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.models.GlassResponse
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