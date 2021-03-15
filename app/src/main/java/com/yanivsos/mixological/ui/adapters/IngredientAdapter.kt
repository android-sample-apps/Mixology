package com.yanivsos.mixological.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.databinding.ListItemIngredientBinding
import com.yanivsos.mixological.databinding.ListItemIngredientLoadingBinding
import com.yanivsos.mixological.extensions.layoutInflater
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.models.LoadingIngredientUiModel
import com.yanivsos.mixological.ui.utils.InputActions

private const val LOADING = 0
private const val LOADED = 1

class IngredientAdapter :
    DiffAdapter<LoadingIngredientUiModel, IngredientAdapter.IngredientViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (getData(position)) {
            is LoadingIngredientUiModel.Loading -> LOADING
            is LoadingIngredientUiModel.Loaded -> LOADED
        }
    }

    override fun onBindViewHolder(
        holder: IngredientViewHolder,
        data: LoadingIngredientUiModel,
        position: Int
    ) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return if (viewType == LOADED) {
            IngredientLoadedViewHolder(
                ListItemIngredientBinding.inflate(
                    parent.layoutInflater(),
                    parent,
                    false
                )
            )
        } else {
            IngredientLoadingViewHolder(
                ListItemIngredientLoadingBinding.inflate(
                    parent.layoutInflater(),
                    parent,
                    false
                )
            )
        }
    }

    abstract class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: LoadingIngredientUiModel)
    }

    inner class IngredientLoadingViewHolder(binding: ListItemIngredientLoadingBinding) :
        IngredientViewHolder(binding.root) {
        override fun bind(data: LoadingIngredientUiModel) {
            //do nothing
        }
    }

    inner class IngredientLoadedViewHolder(private val binding: ListItemIngredientBinding) :
        IngredientViewHolder(binding.root) {

        override fun bind(data: LoadingIngredientUiModel) {
            val ingredient = (data as? LoadingIngredientUiModel.Loaded)?.ingredient ?: return
            binding.run {
                ingredientNameTv.text = ingredient.name

                ingredientQuantityTv.run {
                    text = ingredient.quantity
                    visibility = (ingredient.quantity.isNotEmpty()).toVisibility()
                }
                root.setOnLongClickListener {
                    sendInputAction(
                        InputActions.LongClick(
                            LoadingIngredientUiModel.Loaded(
                                ingredient
                            )
                        )
                    )
                    true
                }
            }
        }
    }
}


