package com.zemingo.drinksmenu.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.viewHolderInflate(@LayoutRes resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, this, false)
}