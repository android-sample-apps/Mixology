package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.models.AlcoholicFilterResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
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