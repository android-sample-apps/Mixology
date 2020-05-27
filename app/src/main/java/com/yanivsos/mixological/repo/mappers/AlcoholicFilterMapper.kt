package com.yanivsos.mixological.repo.mappers

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.repo.models.AlcoholicFilterResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import java.util.function.Function

class AlcoholicFilterMapper :
    Function<DrinksWrapperResponse<AlcoholicFilterResponse>, List<AlcoholicFilterModel>> {

    override fun apply(t: DrinksWrapperResponse<AlcoholicFilterResponse>): List<AlcoholicFilterModel> {
        return t
            .data
            .mapNotNull { it.strAlcoholic }
            .filterNot { it.isBlank() }
            .map {
                AlcoholicFilterModel(
                    name = it
                )
            }
    }
}