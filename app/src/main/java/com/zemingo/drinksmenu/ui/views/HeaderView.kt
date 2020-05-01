package com.zemingo.drinksmenu.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.getStringFromResourceId
import kotlinx.android.synthetic.main.list_item_header.view.*

class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.list_item_header, this)
        initAttributes(attrs)
    }

    var headerText: CharSequence? = null
        set(value) {
            field = value
            header_tv.text = value
        }

    var secondaryText: CharSequence? = null
        set(value) {
            field = value
            secondary_tv.text = value
        }

    private fun initAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HeaderView, 0, 0
        ).apply {
            try {
                headerText = getStringFromResourceId(R.styleable.HeaderView_headerText)
                secondaryText = getStringFromResourceId(R.styleable.HeaderView_secondaryText)
            } finally {
                recycle()
            }
        }
    }


}