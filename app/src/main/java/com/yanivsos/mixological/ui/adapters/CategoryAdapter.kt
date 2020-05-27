package com.yanivsos.mixological.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.viewHolderInflate
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter :
    DiffAdapter<CategoryUiModel, CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(data: CategoryUiModel) {
            containerView.apply {
                setOnClickListener { sendInputAction(InputActions.Click(data)) }
                category_name_tv.text = data.name
            }
        }
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        data: CategoryUiModel,
        position: Int
    ) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            parent.viewHolderInflate(R.layout.list_item_category)
        )
    }
}