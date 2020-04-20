package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.fromLink(link: String?) {
    Glide.with(context).load(link).into(this)
}