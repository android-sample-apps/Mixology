package com.zemingo.drinksmenu.ui.adapters

import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_method.view.*

class MethodAdapter : DiffAdapter<SpannableString, MethodAdapter.MethodViewHolder>() {

    inner class MethodViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(method: SpannableString) {
            containerView.run {
                method_tv.text = method
            }
        }
    }

    override fun onBindViewHolder(holder: MethodViewHolder, data: SpannableString, position: Int) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        return MethodViewHolder(
            parent.viewHolderInflate(R.layout.list_item_method)
        )
    }
}