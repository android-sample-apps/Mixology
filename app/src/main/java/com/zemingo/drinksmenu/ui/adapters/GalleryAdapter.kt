/*
package com.zemingo.drinksmenu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_drink_preview.view.*

class GalleryAdapter() : DiffAdapter<DrinkPreviewUiModel, GalleryAdapter.DrinkPreviewViewHolder>() {

    var onClick: ((DrinkPreviewUiModel) -> Unit)? = null

    inner class DrinkPreviewViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            containerView.apply {
                thumbnail_iv.fromLink(drinkPreviewUiModel.thumbnail)
                name_tv.text = drinkPreviewUiModel.name
                setOnClickListener { onClick?.invoke(drinkPreviewUiModel) }
            }
        }
    }

    val spanSizeLookup: GridLayoutManager.SpanSizeLookup by lazy {
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return items[position].columns
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].columns
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkPreviewViewHolder {
        return DrinkPreviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_drink_preview, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: DrinkPreviewViewHolder,
        data: DrinkPreviewUiModel,
        position: Int
    ) {
        TODO("Not yet implemented")
    }
}*/
