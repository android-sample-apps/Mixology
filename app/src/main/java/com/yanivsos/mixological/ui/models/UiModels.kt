package com.yanivsos.mixological.ui.models

import android.os.Parcelable
import android.text.SpannableString
import androidx.annotation.Keep
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.FilterType
import kotlinx.parcelize.Parcelize

data class CategoryUiModel(
    val name: String
)

@Keep
@Parcelize
data class DrinkPreviewUiModel(
    val id: String,
    val name: String,
    val thumbnail: String?,
    val isFavorite: Boolean
) : Parcelable {

    constructor(drinkUiModel: DrinkUiModel) : this(
        drinkUiModel.id,
        drinkUiModel.name,
        drinkUiModel.thumbnail,
        drinkUiModel.isFavorite
    )
}

data class DrinkUiModel(
    val id: String,
    val name: String,
    val instructions: List<SpannableString>,
    val ingredients: List<IngredientUiModel>,
    val category: String,
    val alcoholic: String?,
    val glass: String,
    val video: String?,
    val thumbnail: String?,
    val shareText: String,
    val isFavorite: Boolean
)

data class LandingPageUiModel(
    val mostPopular: List<DrinkPreviewUiModel>,
    val latestArrivals: List<DrinkPreviewUiModel>,
    val recentSearches: List<DrinkPreviewUiModel>
)

data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val thumbnail: String? = null
)

sealed class LoadingMethodUiModel {
    object Loading : LoadingMethodUiModel()
    data class Loaded(val method: SpannableString) : LoadingMethodUiModel()
}

sealed class LoadingIngredientUiModel {
    object Loading : LoadingIngredientUiModel()
    data class Loaded(val ingredient: IngredientUiModel) : LoadingIngredientUiModel()
}

data class IngredientFilterUiModel(
    val name: String
)

data class IngredientDetailsUiModel(
    val name: String,
    val description: String?,
    val image: String,
    val isAlcoholic: Boolean,
    val alcoholVolume: String?
)

data class GlassUiModel(
    val name: String
)

data class AlcoholFilterUiModel(
    val name: String
)

data class DrinkFilterUiModel(
    val name: String,
    val drinkFilter: DrinkFilter,
    val selected: Boolean = false
)

data class SearchFiltersUiModel(
    val filters: Map<FilterType, List<DrinkFilterUiModel>>,
    val activeFilters: Map<FilterType, Int?>
) {
    val activeFiltersBadge = activeFiltersBadge()

    private fun activeFiltersBadge(): Int? {
        val activeFilters = countActiveFilters()
        return if (activeFilters > 0) {
            activeFilters
        } else {
            null
        }
    }

    private fun countActiveFilters(): Int {
        var size = 0
        activeFilters
            .mapNotNull { it.value }
            .forEach { size += it }
        return size
    }
}

@Keep
@Parcelize
data class DrinkErrorUiModel(
    val drinkId: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @RawRes val lottieAnimation: Int
) : Parcelable

sealed class ResultUiModel<T> {
    data class Success<T>(val data: T) : ResultUiModel<T>()
    data class Loading<T>(val id: String) : ResultUiModel<T>()
    data class Error<T>(val errorUiModel: DrinkErrorUiModel) : ResultUiModel<T>()
}
