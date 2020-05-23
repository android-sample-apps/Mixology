package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewGridAdapter
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.utils.InputActions
import com.zemingo.drinksmenu.ui.view_model.WatchlistViewModel
import kotlinx.android.synthetic.main.fragment_watchlist.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import org.koin.android.viewmodel.ext.android.viewModel

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private val watchlistViewModel: WatchlistViewModel by viewModel()
    private val previewAdapter = DrinkPreviewGridAdapter()

    private fun onDrinkClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(
                childFragmentManager,
                DrinkPreviewOptionsBottomFragment.TAG
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeDrinkClicks()
        observeDrinkLongClicks()
    }

    private fun observeDrinkLongClicks() {
        lifecycleScope.launchWhenStarted {
            previewAdapter
                .inputActions
                .filterIsInstance<InputActions.LongClick<DrinkPreviewUiModel>>()
                .map { it.data }
                .collect { onDrinkLongClicked(it) }
        }
    }

    private fun observeDrinkClicks() {
        lifecycleScope.launchWhenStarted {
            previewAdapter
                .inputActions
                .filterIsInstance<InputActions.Click<DrinkPreviewUiModel>>()
                .map { it.data }
                .collect { onDrinkClicked(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWatchlistRecyclerView()
        observeWatchlist()
    }

    private fun initWatchlistRecyclerView() {
        watchlist_rv.run {
            adapter = previewAdapter
            addItemDecoration(
                GridSpacerItemDecoration(
                    right = 4.dpToPx().toInt(),
                    left = 4.dpToPx().toInt(),
                    bottom = 4.dpToPx().toInt()
                )
            )
        }
    }

    private fun observeWatchlist() {
        watchlistViewModel
            .watchlist
            .observe(viewLifecycleOwner, Observer {
                previewAdapter.update(it)
            })
    }
}