package com.yanivsos.mixological.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import timber.log.Timber

typealias FBParam = FirebaseAnalytics.Param
typealias FBEvent = FirebaseAnalytics.Event

object AnalyticsDispatcher {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun onDrinkPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        Timber.d("onDrinkPreviewClicked: origin[$origin], drinkPreview[$drinkPreviewUiModel]")
        firebaseAnalytics.logEvent(FBEvent.SELECT_ITEM) {
            param(FBParam.ORIGIN, origin)
            param(FBParam.ITEM_ID, drinkPreviewUiModel.id)
            param(FBParam.ITEM_NAME, drinkPreviewUiModel.name)
            param(PARAM_IS_FAVORITE, drinkPreviewUiModel.isFavorite.toString())
        }
    }

    fun onDrinkPreviewLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        Timber.d("onDrinkPreviewLongClicked: origin[$origin], drinkPreview[$drinkPreviewUiModel]")
        firebaseAnalytics.logEvent(EVENT_DRINK_LONG_CLICK) {
            param(FBParam.ORIGIN, origin)
            param(FBParam.ITEM_ID, drinkPreviewUiModel.id)
            param(FBParam.ITEM_NAME, drinkPreviewUiModel.name)
            param(PARAM_IS_FAVORITE, drinkPreviewUiModel.isFavorite.toString())
        }
    }

    fun onDrinkShare(drinkPreviewUiModel: DrinkPreviewUiModel) {
        firebaseAnalytics.logEvent(FBEvent.SHARE) {
            param(FBParam.ITEM_ID, drinkPreviewUiModel.id)
            param(FBParam.ITEM_NAME, drinkPreviewUiModel.name)
        }
    }

    fun addToFavorites(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {

    }

    fun removeFromFavorites(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {

    }
}

class ScreenNames {
    companion object {
        const val LANDING_PAGE = ""
        const val CATEGORIES = ""
        const val FAVORITES = ""
        const val SEARCH = ""
        const val FAILURE = ""
    }
}

private const val EVENT_DRINK_LONG_CLICK = "drink_long_click"


private const val PARAM_IS_FAVORITE = "is_favorite"
