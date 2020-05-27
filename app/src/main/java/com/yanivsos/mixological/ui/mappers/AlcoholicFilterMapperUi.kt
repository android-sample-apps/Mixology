package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.ui.models.AlcoholFilterUiModel
import java.util.function.Function

class AlcoholicFilterMapperUi : Function<List<AlcoholicFilterModel>, List<AlcoholFilterUiModel>> {

    override fun apply(t: List<AlcoholicFilterModel>): List<AlcoholFilterUiModel> {
        return t.map { AlcoholFilterUiModel(name = it.name) }
    }
}