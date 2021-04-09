package com.yanivsos.mixological.v2.search.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ViewChoiceFilterBinding
import com.yanivsos.mixological.extensions.getStringFromResourceId

class FilterChoiceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    private val binding: ViewChoiceFilterBinding = ViewChoiceFilterBinding.bind(
        inflate(context, R.layout.view_choice_filter, this)
    )

    var text: CharSequence? = null
        set(value) {
            binding.filterTv.text = value
            field = value
        }

    init {
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FilterChoiceView, 0, 0
        ).apply {
            try {
                text = getStringFromResourceId(R.styleable.FilterChoiceView_fcv_title)
            } finally {
                recycle()
            }
        }
    }
}
