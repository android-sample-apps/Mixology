package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.domain.models.GlassModel
import com.yanivsos.mixological.ui.models.GlassUiModel
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