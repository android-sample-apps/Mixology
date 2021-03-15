package com.yanivsos.mixological.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.databinding.TileItemDrinkPreviewBinding
import com.yanivsos.mixological.extensions.fromLink
import com.yanivsos.mixological.extensions.layoutInflater
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.InputActions

class DrinkPreviewAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewAdapter.DrinkPreviewViewHolder>() {

    inner class DrinkPreviewViewHolder(private val binding: TileItemDrinkPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            binding.apply {
                drinkImageIv
                drinkImageIv.fromLink(drinkPreviewUiModel.thumbnail)
                drinkNameTv.text = drinkPreviewUiModel.name
                cherryBadgeContainer
                cherryBadgeContainer.root.visibility = drinkPreviewUiModel.isFavorite.toVisibility()
                imageContainer.run {
                    setOnClickListener {
                        sendInputAction(InputActions.Click(drinkPreviewUiModel))
                    }
                    setOnLongClickListener {
                        sendInputAction(InputActions.LongClick(drinkPreviewUiModel))
                        true
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: DrinkPreviewViewHolder,
        data: DrinkPreviewUiModel,
        position: Int
    ) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkPreviewViewHolder {
        return DrinkPreviewViewHolder(
            TileItemDrinkPreviewBinding.inflate(
                parent.layoutInflater(),
                parent,
                false
            )
//            parent.viewHolderInflate(R.layout.tile_item_drink_preview)
        )
    }
}
