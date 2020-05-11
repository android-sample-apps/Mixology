package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.domain.models.SearchFiltersModel
import com.zemingo.drinksmenu.ui.models.SearchFiltersUiModel
import java.util.function.Function

class SearchFiltersMapperUi(
    private val alcoholicFilterMapperUi: AlcoholicFilterMapperUi,
    private val categoryMapperUi: CategoryMapperUi,
    private val ingredientFilterMapperUi: IngredientFilterMapperUi,
    private val glassMapperUi: GlassMapperUi
) : Function<SearchFiltersModel, SearchFiltersUiModel> {

    override fun apply(t: SearchFiltersModel): SearchFiltersUiModel {
        return SearchFiltersUiModel(
            categories = categoryMapperUi.apply(t.categories),
            alcoholic = alcoholicFilterMapperUi.apply(t.alcoholic),
            ingredients = ingredientFilterMapperUi.apply(t.ingredients),
            glasses = glassMapperUi.apply(t.glasses)
        )
    }
}