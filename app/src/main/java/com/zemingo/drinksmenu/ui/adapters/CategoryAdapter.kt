package com.zemingo.drinksmenu.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.models.CategoryUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter :
    DiffAdapter<CategoryUiModel, CategoryAdapter.CategoryViewHolder>() {

    var onClick: ((CategoryUiModel) -> Unit)? = null

    inner class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(data: CategoryUiModel) {
            containerView.apply {
                setOnClickListener { onClick?.invoke(data) }
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