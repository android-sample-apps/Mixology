package com.yanivsos.mixological.analytics

import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import timber.log.Timber

typealias FBParam = FirebaseAnalytics.Param
typealias FBEvent = FirebaseAnalytics.Event

object AnalyticsDispatcher {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun onDrinkPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        Timber.d("onDrinkPreviewClicked: origin[$origin], drinkPreview[$drinkPreviewUiModel]")
        firebaseAnalytics.logEvent(Events.EVENT_DRINK_SELECTED) {
            idAndName(drinkPreviewUiModel)
            param(FBParam.ORIGIN, origin)
            param(PARAM_IS_FAVORITE, drinkPreviewUiModel.isFavorite.toString())
        }
    }

    fun onDrinkPreviewLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        Timber.d("onDrinkPreviewLongClicked: origin[$origin], drinkPreview[$drinkPreviewUiModel]")
        firebaseAnalytics.logEvent(Events.EVENT_DRINK_LONG_CLICK) {
            idAndName(drinkPreviewUiModel)
            param(FBParam.ORIGIN, origin)
            param(PARAM_IS_FAVORITE, drinkPreviewUiModel.isFavorite.toString())
        }
    }

    fun onDrinkShare(drinkPreviewUiModel: DrinkPreviewUiModel) {
        firebaseAnalytics.logEvent(FBEvent.SHARE) {
            idAndName(drinkPreviewUiModel)
        }
    }

    fun toggleFavorites(
        drinkPreviewUiModel: DrinkPreviewUiModel,
        isFavorite: Boolean,
        origin: String
    ) {
        if (isFavorite) {
            addToFavorites(drinkPreviewUiModel, origin)
        } else {
            removeFromFavorites(drinkPreviewUiModel, origin)
        }
    }

    fun addToFavorites(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        firebaseAnalytics.logEvent(Events.EVENT_ADD_TO_FAVORITE) {
            idAndName(drinkPreviewUiModel)
            param(FBParam.ORIGIN, origin)
        }
    }

    fun removeFromFavorites(drinkPreviewUiModel: DrinkPreviewUiModel, origin: String) {
        firebaseAnalytics.logEvent(Events.EVENT_REMOVE_FROM_FAVORITE) {
            idAndName(drinkPreviewUiModel)
            param(FBParam.ORIGIN, origin)
        }
    }

    fun onIngredientLongClicked(ingredientUiModel: IngredientUiModel, origin: String) {
        firebaseAnalytics.logEvent(Events.EVENT_INGREDIENT_LONG_CLICK) {
            param(FBParam.ITEM_NAME, ingredientUiModel.name)
            param(FBParam.ORIGIN, origin)
        }
    }

    fun onIngredientSearchedOnline(ingredientUiModel: IngredientUiModel) {
        firebaseAnalytics.logEvent(Events.INGREDIENT_SEARCH_ONLINE) {
            param(FBParam.ITEM_NAME, ingredientUiModel.name)
        }
    }

    fun onDrinkErrorTryAgain(errorUiModel: DrinkErrorUiModel) {
        firebaseAnalytics.logEvent(Events.DRINK_TRY_AGAIN_CLICKED) {
            param(FBParam.ITEM_ID, errorUiModel.drinkId)
        }
    }

    fun onSearchFilter(drinkFilter: DrinkFilter) {
        firebaseAnalytics.logEvent(Events.SEARCH_DRINK) {
            param(FBParam.SEARCH_TERM, drinkFilter.query)
            param(FBParam.CONTENT_TYPE, drinkFilter.type.name)
            param(PARAM_IS_ACTIVE, drinkFilter.active.toString())
        }
    }

    fun setCurrentScreen(fragment: Fragment) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, fragment::class.simpleName!!)
        }
    }

    fun onInAppReviewLaunched() {
        firebaseAnalytics.logEvent(Events.EVENT_LAUNCH_IN_APP_REVIEW, null)
    }

    private fun ParametersBuilder.idAndName(drinkPreviewUiModel: DrinkPreviewUiModel) {
        param(FBParam.ITEM_ID, drinkPreviewUiModel.id)
        param(FBParam.ITEM_NAME, drinkPreviewUiModel.name)
    }
}

class ScreenNames {
    companion object {
        const val LANDING_PAGE = "Landing Page"
        const val CATEGORIES = "Categories"
        const val INGREDIENTS = "Ingredients"
        const val DRINK = "Drink"
        const val FAVORITES = "Favorites"
        const val SEARCH = "Search"
//        const val ERROR = "Error"
        const val DRINK_OPTIONS = "Drink Options"
    }
}

class Events {
    companion object {
        const val EVENT_DRINK_SELECTED = "drink_selected"
        const val EVENT_DRINK_LONG_CLICK = "drink_long_click"
        const val EVENT_ADD_TO_FAVORITE = "add_to_favorite"
        const val EVENT_REMOVE_FROM_FAVORITE = "remove_from_favorite"
        const val EVENT_INGREDIENT_LONG_CLICK = "ingredient_long_clicked"
        const val EVENT_LAUNCH_IN_APP_REVIEW = "launch_in_app_review"
        const val INGREDIENT_SEARCH_ONLINE = "ingredient_search_online"
        const val DRINK_TRY_AGAIN_CLICKED = "drink_try_again_clicked"
        const val SEARCH_DRINK = "drink_search"
    }
}


private const val PARAM_IS_FAVORITE = "is_favorite"
private const val PARAM_IS_ACTIVE = "is_active"
