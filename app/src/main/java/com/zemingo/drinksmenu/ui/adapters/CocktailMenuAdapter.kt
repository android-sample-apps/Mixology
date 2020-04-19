package com.zemingo.drinksmenu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.models.DrinkItemUiModel
import com.zemingo.drinksmenu.models.DrinkPreviewItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_cocktail.view.*

class CocktailMenuAdapter : DiffAdapter<DrinkPreviewItemUiModel, CocktailMenuAdapter.CocktailMenuVH>() {

    override fun onBindViewHolder(
        holder: CocktailMenuVH,
        data: DrinkPreviewItemUiModel,
        position: Int
    ) {
        return holder.bind(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailMenuVH {
        return CocktailMenuVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_cocktail, parent, false)
        )
    }

    inner class CocktailMenuVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(uiModel: DrinkPreviewItemUiModel, position: Int) {
            containerView.apply {
                name_tv.text = uiModel.name
//                ingredients_tv.text = uiModel.ingredients
//                bindGlassImage(position, uiModel)
                bindThumbnail(uiModel.thumbnail)
            }
        }

        private fun bindGlassImage(
            position: Int,
            uiModel: DrinkItemUiModel
        ) {
            containerView.apply {
                if (position.rem(2) == 0) {
                    right_glass_iv.visibility = View.GONE
                    left_glass_iv.visibility = View.VISIBLE
                    left_glass_iv.setImageResource(uiModel.glassIcon)
                } else {
                    left_glass_iv.visibility = View.GONE
                    right_glass_iv.visibility = View.VISIBLE
                    right_glass_iv.setImageResource(uiModel.glassIcon)
                }
            }
        }
        private fun bindThumbnail(imageModel: String?) {
            containerView.apply {
//                imageModel?.into(thumbnail_iv)
            }
        }

    }

}