package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentWatchlistBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.DrinkPreviewGridAdapter
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.view_model.WatchlistViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseFragment(R.layout.fragment_watchlist) {

    private val binding by viewBinding(FragmentWatchlistBinding::bind)
    private val watchlistViewModel: WatchlistViewModel by viewModel()
    private val previewAdapter = DrinkPreviewGridAdapter()

    private fun onDrinkClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreviewUiModel, ScreenNames.FAVORITES)
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreviewUiModel, ScreenNames.FAVORITES)
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
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
        binding.watchlistRv.run {
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
            .observe(viewLifecycleOwner, {
                updateWatchlist(it)
            })
    }

    private fun updateWatchlist(watchlist: List<DrinkPreviewUiModel>) {
        binding.favoriteInstructionsContainer.root.visibility = watchlist.isEmpty().toVisibility()
        previewAdapter.update(watchlist)
    }
}
