package com.zemingo.cocktailmenu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.cocktailmenu.R
import com.zemingo.cocktailmenu.models.CocktailItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_cocktail.view.*

class CocktailMenuAdapter : DiffAdapter<CocktailItemUiModel, CocktailMenuAdapter.CocktailMenuVH>() {

    inner class CocktailMenuVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(uiModel: CocktailItemUiModel, position: Int) {
            containerView.apply {
                name_tv.text = uiModel.name
                ingredients_tv.text = uiModel.ingredients
                bindGlassImage(position, uiModel)
            }
        }

        private fun bindGlassImage(
            position: Int,
            uiModel: CocktailItemUiModel
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
    }

    override fun onBindViewHolder(
        holder: CocktailMenuVH,
        data: CocktailItemUiModel,
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

}