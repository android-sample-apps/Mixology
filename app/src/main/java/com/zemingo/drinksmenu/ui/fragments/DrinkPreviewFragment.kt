package com.zemingo.drinksmenu.ui.fragments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tile_item_drink_preview.view.*

/*class DrinkPreviewFragment : Fragment(R.layout.fragment_drink_preview) {

//    private val args: DrinkPreviewFragmentArgs by navArgs()
    private val drinkPreviewViewModel: DrinkPreviewByCategoryViewModel by viewModel {
        parametersOf(
            args.category
        )
    }
    private val drinkAdapter = DrinkPreviewAdapter()
        .apply {
            onClick = { onItemClicked(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        drinkPreviewViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer { drinkAdapter.update(it) })
    }

    private fun initRecyclerView() {
        drink_preview_rv.apply {
//            addItemDecoration(
//                VerticalSpaceItemDecoration(30)
//            )
            adapter = drinkAdapter
        }
    }

    private fun onItemClicked(drinkPreview: DrinkPreviewUiModel) {
//        findNavController().navigate(
//            DrinkPreviewFragmentDirections.actionDrinkPreviewFragmentToDrinkFragment(drinkPreview.id)
//        )
    }
}*/

