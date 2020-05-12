package com.zemingo.drinksmenu.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.compatColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}