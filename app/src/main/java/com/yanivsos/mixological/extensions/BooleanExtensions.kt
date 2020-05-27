package com.yanivsos.mixological.extensions

import android.view.View

fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE