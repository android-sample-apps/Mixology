package com.zemingo.drinksmenu.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tile_item_drink_preview_grid.view.*
import timber.log.Timber

class DrinkPreviewGridAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewGridAdapter.GridDrinkPreviewViewHolder>() {

    var onClick: ((DrinkPreviewUiModel) -> Unit)? = null

    inner class GridDrinkPreviewViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            containerView.run {
                drink_image_iv.fromLink(drinkPreviewUiModel.thumbnail)
                drink_name_tv.text = drinkPreviewUiModel.name
                image_container.setOnClickListener {
                    Timber.d("clicked from $drinkPreviewUiModel with $this ")
                    onClick?.invoke(drinkPreviewUiModel)
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
            parent.viewHolderInflate(R.layout.tile_item_drink_preview_grid)
        )
    }
}