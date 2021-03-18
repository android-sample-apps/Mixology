package com.yanivsos.mixological.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.databinding.ListItemCategoryBinding
import com.yanivsos.mixological.extensions.layoutInflater
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.utils.InputActions

class CategoryAdapter :
    DiffAdapter<CategoryUiModel, CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryUiModel) {
            binding.root.apply {
                setOnClickListener { sendInputAction(InputActions.Click(data)) }
                binding.categoryNameTv.text = data.name
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
        val binding = ListItemCategoryBinding.inflate(
            parent.layoutInflater(),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }
}
