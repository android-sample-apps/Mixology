package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.domain.models.SearchFiltersModel
import com.zemingo.drinksmenu.ui.models.DrinkFilterUiModel
import com.zemingo.drinksmenu.ui.models.SearchFiltersUiModel
import java.util.function.Function

class SearchFiltersMapperUi : Function<SearchFiltersModel, SearchFiltersUiModel> {

    override fun apply(t: SearchFiltersModel): SearchFiltersUiModel {
        return SearchFiltersUiModel(
            filters = mutableMapOf<FilterType, List<DrinkFilterUiModel>>().apply {
                put(
                    FilterType.CATEGORY,
                    t.categories.map {
                        DrinkFilterUiModel(
                            name = it.name,
                            drinkFilter = DrinkFilter(it.name, FilterType.CATEGORY)
                        )
                    })
                put(
                    FilterType.ALCOHOL,
                    t.alcoholic.map {
                        DrinkFilterUiModel(
                            name = it.name,
                            drinkFilter = DrinkFilter(it.name, FilterType.ALCOHOL)
                        )
                    })
                put(
                    FilterType.INGREDIENTS,
                    t.ingredients.map {
                        DrinkFilterUiModel(
                            name = it.name,
                            drinkFilter = DrinkFilter(it.name, FilterType.INGREDIENTS)
                        )
                    })
                put(
                    FilterType.GLASS,
                    t.glasses.map {
                        DrinkFilterUiModel(
                            name = it.name,
                            drinkFilter = DrinkFilter(it.name, FilterType.GLASS)
                        )
                    })
            },
            activeFilters = emptyMap()
        )
    }
}