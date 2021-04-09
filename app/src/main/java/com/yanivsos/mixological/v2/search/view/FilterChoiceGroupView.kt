package com.yanivsos.mixological.v2.search.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ViewFilterChoiceGroupBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

class FilterChoiceGroupView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding = ViewFilterChoiceGroupBinding.bind(
        inflate(context, R.layout.view_filter_choice_group, this)
    )
    private val selectedFilterMutableFlow = MutableStateFlow<SelectedFilterChoice?>(null)
    val selectedFilterFlow = selectedFilterMutableFlow.filterNotNull()

    init {
        initViews()
    }

    fun setSelected(filterChoice: SelectedFilterChoice) {
        Timber.d("setSelected: $filterChoice")
        when(filterChoice) {
            SelectedFilterChoice.And -> binding.run {
                andChoice.isSelected = true
                orChoice.isSelected = false
            }
            SelectedFilterChoice.Or -> binding.run {
                andChoice.isSelected = false
                orChoice.isSelected = true
            }
        }
    }

    private fun initViews() {
        binding.andChoice.setOnClickListener {
            notifySelected(SelectedFilterChoice.And)
        }

        binding.orChoice.setOnClickListener {
            notifySelected(SelectedFilterChoice.Or)
        }
    }

    private fun notifySelected(filterChoice: SelectedFilterChoice) {
        selectedFilterMutableFlow.value = filterChoice
    }

    sealed class SelectedFilterChoice {
        object And : SelectedFilterChoice()
        object Or : SelectedFilterChoice()
    }
}

