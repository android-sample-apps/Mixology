package com.zemingo.drinksmenu.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.toVisibility
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.utils.InputActions
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
                image_container.setOnClickListener {
                    sendInputAction(InputActions.Click(drinkPreviewUiModel))
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
