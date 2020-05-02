package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.ui.view_model.DrinkPreviewByCategoryViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_drink_preview.*
import kotlinx.android.synthetic.main.tile_item_drink_preview.view.*
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
        .apply {
            onClick = { onItemClicked(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        drinkPreviewViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer { adapter.update(it) })
    }

    private fun initRecyclerView() {
        drink_preview_rv.apply {
//            addItemDecoration(
//                VerticalSpaceItemDecoration(30)
//            )
            adapter = this@DrinkPreviewFragment.adapter
        }
    }

    private fun onItemClicked(drinkPreview: DrinkPreviewUiModel) {
        findNavController().navigate(
            DrinkPreviewFragmentDirections.actionDrinkPreviewFragmentToDrinkFragment(drinkPreview.id)
        )
    }
}

class DrinkPreviewAdapter :
    DiffAdapter<DrinkPreviewUiModel, DrinkPreviewAdapter.DrinkPreviewViewHolder>() {

    var onClick: ((DrinkPreviewUiModel) -> Unit)? = null

    inner class DrinkPreviewViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(drinkPreviewUiModel: DrinkPreviewUiModel) {
            containerView.apply {
                drink_image_iv.fromLink(drinkPreviewUiModel.thumbnail)
                drink_name_tv.text = drinkPreviewUiModel.name
                setOnClickListener { onClick?.invoke(drinkPreviewUiModel) }
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