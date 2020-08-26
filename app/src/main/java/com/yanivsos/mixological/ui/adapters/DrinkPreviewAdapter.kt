package com.yanivsos.mixological.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.fromLink
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.extensions.viewHolderInflate
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tile_item_drink_preview.view.*

class DrinkPreviewAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewAdapter.DrinkPreviewViewHolder>() {

    inner class DrinkPreviewViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            containerView.apply {
                drink_image_iv.fromLink(drinkPreviewUiModel.thumbnail)
                drink_name_tv.text = drinkPreviewUiModel.name
                cherry_badge_container.visibility = drinkPreviewUiModel.isFavorite.toVisibility()
                image_container.run {
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
            parent.viewHolderInflate(R.layout.tile_item_drink_preview)
        )
    }
}
