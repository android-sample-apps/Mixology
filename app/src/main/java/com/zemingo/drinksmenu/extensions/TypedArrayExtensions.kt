package com.zemingo.drinksmenu.extensions

import android.content.res.TypedArray
import androidx.annotation.StyleableRes

fun TypedArray.getStringFromResourceId(@StyleableRes resId: Int): String? {
    val resource = getResourceId(resId, 0)
    return resources.getString(resource)
}