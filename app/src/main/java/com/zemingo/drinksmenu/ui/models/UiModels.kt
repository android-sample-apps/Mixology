package com.zemingo.drinksmenu.ui.models

import android.os.Parcelable
import android.text.SpannableString
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.extensions.dpToPx
import kotlinx.android.parcel.Parcelize

data class CategoryUiModel(
    val name: String
)

data class DrinkPreviewUiModel(
    val id: String,
    val name: String,
    val thumbnail: String?,
    val isFavorite: Boolean
)

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
    val shareText: String
)

data class LandingPageUiModel(
    val mostPopular: List<DrinkPreviewUiModel>,
    val latestArrivals: List<DrinkPreviewUiModel>,
    val recentSearches: List<DrinkPreviewUiModel>
)

data class IngredientUiModel(
    val name: String,
    val quantity: String?,
    val thumbnail: String? = null
)

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
    var selected: Boolean = false
) {
    val color: Int get() = if (selected) R.color.header_text_color else R.color.secondary_text_color
    val alpha: Float get() = if (selected) 1f else 0.5f
    val elevation: Float get() = if (selected) 8.dpToPx() else 0.dpToPx()
}

data class SearchFiltersUiModel(
    val filters: Map<FilterType, List<DrinkFilterUiModel>>,
    val activeFilters: Map<FilterType, Int?>
) {
    val activeFiltersBadge = activeFiltersBadge()

    private fun activeFiltersBadge(): String? {
        val activeFilters = countActiveFilters()
        return if (activeFilters > 0) {
            activeFilters.toString()
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