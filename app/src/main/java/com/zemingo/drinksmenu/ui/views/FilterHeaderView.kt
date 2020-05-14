package com.zemingo.drinksmenu.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.getStringFromResourceId
import kotlinx.android.synthetic.main.active_filter_badge.view.*
import kotlinx.android.synthetic.main.view_filter_header.view.*

class FilterHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    var filters: String? = null
        set(value) {
            field = value
            showFilters(filters)
            invalidate()
        }

    var headerText: CharSequence? = null
        set(value) {
            header_tv.text = value
            field = value
        }

    init {
        View.inflate(context, R.layout.view_filter_header, this)
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FilterHeaderView, 0, 0
        ).apply {
            try {
                headerText = getStringFromResourceId(R.styleable.FilterHeaderView_fhv_headerText)
            } finally {
                recycle()
            }
        }
    }

    private fun showFilters(filters: String?) {
        if (filters != null) {
            visibleBadge()
        }
        else {
            invisibleBadge()
        }
        filter_tv.text = filters
    }

    private fun invisibleBadge() {
        transitionToStart()
    }

    private fun visibleBadge() {
        transitionToEnd()
    }
}