package com.zemingo.drinksmenu.extensions

import android.view.View

fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE