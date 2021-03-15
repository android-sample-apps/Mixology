package com.yanivsos.mixological.ui.adapters

import android.text.SpannableString
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yanivsos.mixological.databinding.ListItemMethodBinding
import com.yanivsos.mixological.databinding.ListItemMethodLoadingBinding
import com.yanivsos.mixological.extensions.layoutInflater
import com.yanivsos.mixological.ui.models.LoadingMethodUiModel

private const val LOADING = 0
private const val LOADED = 1

class MethodAdapter : DiffAdapter<LoadingMethodUiModel, MethodAdapter.MethodViewHolder>() {

    inner class MethodViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(method: SpannableString) {
            (binding as? ListItemMethodBinding)?.run {
                methodTv.text = method
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
        val inflater = parent.layoutInflater()
        val layout = if (viewType == LOADING) {
            ListItemMethodLoadingBinding.inflate(inflater, parent, false)
        } else {
            ListItemMethodBinding.inflate(inflater, parent, false)
        }
        return MethodViewHolder(
            layout
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
