package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.view_model.DrinkPreviewByCategoryViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_drink_preview.*
import kotlinx.android.synthetic.main.list_item_drink_preview.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DrinkPreviewFragment : Fragment(R.layout.fragment_drink_preview) {

    private val args: DrinkPreviewFragmentArgs by navArgs()
    private val drinkPreviewViewModel: DrinkPreviewByCategoryViewModel by viewModel {
        parametersOf(
            args.category
        )
    }
    private val adapter = DrinkPreviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drink_preview_rv.adapter = adapter
        drinkPreviewViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer { adapter.update(it) })
    }
}

class DrinkPreviewAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewAdapter.DrinkPreviewViewHolder>() {

    inner class DrinkPreviewViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            containerView.apply {
                thumbnail_iv.fromLink(drinkPreviewUiModel.thumbnail)
                name_tv.text = drinkPreviewUiModel.name
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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_drink_preview, parent, false)
        )
    }
}