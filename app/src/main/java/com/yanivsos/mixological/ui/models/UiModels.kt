package com.yanivsos.mixological.ui.models

import android.os.Parcelable
import android.text.SpannableString
import androidx.annotation.Keep
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.yanivsos.mixological.extensions.toVisibility
import kotlinx.parcelize.Parcelize

data class CategoryUiModel(
    val name: String
)

data class SelectedCategoryUiModel(
    val name: String,
    val drinks: List<DrinkPreviewUiModel>
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
    val recentlyViewed: List<DrinkPreviewUiModel>
)

data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val thumbnail: String? = null
)

fun IngredientUiModel.quantityVisibility() = quantity.isNotEmpty().toVisibility()

data class IngredientDetailsUiModel(
    val name: String,
    val description: String?,
    val image: String,
    val isAlcoholic: Boolean,
    val alcoholVolume: String?
)

@Keep
@Parcelize
data class DrinkErrorUiModel(
    val drinkId: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @RawRes val lottieAnimation: Int
) : Parcelable
