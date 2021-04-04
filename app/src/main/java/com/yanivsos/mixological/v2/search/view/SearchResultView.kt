package com.yanivsos.mixological.v2.search.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import com.xwray.groupie.GroupieAdapter
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ViewSearchResultsBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.v2.favorites.fragments.GridDrinkPreviewItem

class SearchResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MotionLayout(context, attrs) {

    private val binding = ViewSearchResultsBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private val previewAdapter = GroupieAdapter()

    init {
        initResultsRecyclerView()
    }

    private fun initResultsRecyclerView() {
        binding.searchResultsRv.run {
            adapter = previewAdapter
            addItemDecoration(
                GridSpacerItemDecoration(
                    right = 4.dpToPx().toInt(),
                    left = 4.dpToPx().toInt(),
                    bottom = 4.dpToPx().toInt()
                )
            )
        }
    }

    fun updateNoResults() {
        previewAdapter.updateAsync(emptyList())
        transitionToState(R.id.no_result)
    }

    fun updateResults(results: List<GridDrinkPreviewItem>) {
        previewAdapter.updateAsync(results)
        transitionToState(R.id.has_results)
    }
}
