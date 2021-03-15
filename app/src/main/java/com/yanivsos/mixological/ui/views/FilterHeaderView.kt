package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ViewFilterHeaderBinding
import com.yanivsos.mixological.extensions.getStringFromResourceId

class FilterHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    var filters: String? = null
        set(value) {
            field = value
            showFilters(filters)
        }

    var onFilterClearedClickedListener: (() -> Unit)? = null
    set(value) {
        field = value
        setOnClearFilterClickListener()
    }

    private var headerText: CharSequence? = null
        set(value) {
            binding.headerTv.text = value
            field = value
        }

    private val binding = ViewFilterHeaderBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {

        initAttributes(attrs)
        setOnClearFilterClickListener()
    }

    private fun setOnClearFilterClickListener() {
        onFilterClearedClickedListener?.let { listener ->
            binding.clearBtn.setOnClickListener { listener.invoke() }
        } ?: binding.clearBtn.setOnClickListener(null)
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
        binding.badge.filterTv.text = filters
        if (filters != null) {
            visibleBadge()
        }
        else {
            invisibleBadge()
        }
    }

    private fun invisibleBadge() {
        transitionToState(R.id.invisible_filter)
    }

    private fun visibleBadge() {
        transitionToState(R.id.visible_filter)
    }
}
