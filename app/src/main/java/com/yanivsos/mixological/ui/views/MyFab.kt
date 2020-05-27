package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.toVisibility
import kotlinx.android.synthetic.main.view_my_fab.view.*

class MyFab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    var text: CharSequence? = null
        set(value) {
            field = value
            fab_tv.text = value
        }

    var textVisibility: Boolean = false
        set(value) {
            field = value
            fab_tv.visibility = value.toVisibility()
        }

    init {
        View.inflate(context, R.layout.view_my_fab, this)
    }
}