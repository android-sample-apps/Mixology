package com.yanivsos.mixological.extensions

import android.os.Bundle
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel

const val ARG_DRINK_PREVIEW_UI_MODEL = "drinkPreviewUiModel"

fun DrinkPreviewUiModel.toBundle(): Bundle {
    return Bundle().also { it.putParcelable(ARG_DRINK_PREVIEW_UI_MODEL, this) }
}

fun Bundle.toDrinkPreviewUiModel(): DrinkPreviewUiModel? {
    return getParcelable(ARG_DRINK_PREVIEW_UI_MODEL)
}