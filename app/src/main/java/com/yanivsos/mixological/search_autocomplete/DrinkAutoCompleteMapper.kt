package com.yanivsos.mixological.search_autocomplete

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import java.util.function.Function

class DrinkAutoCompleteMapper : Function<DrinkPreviewModel, DrinkAutoCompleteModel> {

    override fun apply(t: DrinkPreviewModel): DrinkAutoCompleteModel {
        return DrinkAutoCompleteModel(
            name = t.name,
            image = t.thumbnail
        )
    }
}

class DrinkListAutoCompleteMapper(
    private val singleMapper: DrinkAutoCompleteMapper
) : Function<List<DrinkPreviewModel>, List<DrinkAutoCompleteModel>> {

    override fun apply(t: List<DrinkPreviewModel>): List<DrinkAutoCompleteModel> {
        return t.map {
            singleMapper.apply(it)
        }
    }
}