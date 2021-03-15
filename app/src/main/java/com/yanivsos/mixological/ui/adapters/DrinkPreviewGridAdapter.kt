package com.yanivsos.mixological.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.databinding.TileItemDrinkPreviewGridBinding
import com.yanivsos.mixological.extensions.fromLink
import com.yanivsos.mixological.extensions.layoutInflater
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import timber.log.Timber

class DrinkPreviewGridAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewGridAdapter.GridDrinkPreviewViewHolder>() {

    inner class GridDrinkPreviewViewHolder(private val binding: TileItemDrinkPreviewGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            binding.run {
                drinkImageIv.fromLink(drinkPreviewUiModel.thumbnail/*, R.drawable.bar*/)
                drinkNameTv.text = drinkPreviewUiModel.name
                cherryBadgeContainer.root.visibility = drinkPreviewUiModel.isFavorite.toVisibility()
                imageContainer.setOnClickListener {
                    Timber.d("clicked from $drinkPreviewUiModel with $this ")
                    sendInputAction(InputActions.Click(drinkPreviewUiModel))
                }
                imageContainer.setOnLongClickListener {
                    sendInputAction(InputActions.LongClick(drinkPreviewUiModel))
                    true
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: GridDrinkPreviewViewHolder,
        data: DrinkPreviewUiModel,
        position: Int
    ) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridDrinkPreviewViewHolder {
        return GridDrinkPreviewViewHolder(
            TileItemDrinkPreviewGridBinding.inflate(
                parent.layoutInflater(),
                parent,
                false
            )
        )
    }
}
