package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ListItemHeaderBinding
import com.yanivsos.mixological.extensions.getStringFromResourceId

class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding = ListItemHeaderBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        initAttributes(attrs)
    }

    private var headerText: CharSequence? = null
        set(value) {
            field = value
            binding.alcoholicHeaderFhv.text = value
        }

    private fun initAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HeaderView, 0, 0
        ).apply {
            try {
                headerText = getStringFromResourceId(R.styleable.HeaderView_hv_headerText)
            } finally {
                recycle()
            }
        }
    }


}
