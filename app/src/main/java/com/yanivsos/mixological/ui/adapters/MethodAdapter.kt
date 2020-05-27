package com.yanivsos.mixological.ui.adapters

import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.viewHolderInflate
import com.yanivsos.mixological.ui.models.LoadingMethodUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_method.view.*

private const val LOADING = 0
private const val LOADED = 1

class MethodAdapter : DiffAdapter<LoadingMethodUiModel, MethodAdapter.MethodViewHolder>() {

    inner class MethodViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(method: SpannableString) {
            containerView.run {
                method_tv.text = method
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getData(position)) {
            is LoadingMethodUiModel.Loading -> LOADING
            is LoadingMethodUiModel.Loaded -> LOADED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val layout = if (viewType == LOADING) {
            R.layout.list_item_method_loading
        } else {
            R.layout.list_item_method
        }
        return MethodViewHolder(
            parent.viewHolderInflate(layout)
        )
    }

    override fun onBindViewHolder(
        holder: MethodViewHolder,
        data: LoadingMethodUiModel,
        position: Int
    ) {
        (data as? LoadingMethodUiModel.Loaded)?.let {
            holder.bind(it.method)
        }
    }
}