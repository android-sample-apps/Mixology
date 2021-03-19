package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentLandingPageBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.DrinkPreviewAdapter
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.LandingPageUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.view_model.LandingPageViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

class LandingPageFragment : BaseFragment(R.layout.fragment_landing_page) {

    private val binding by viewBinding(FragmentLandingPageBinding::bind)
    private val landingPageViewModel: LandingPageViewModel by viewModel()
    private val latestArrivalsAdapter = DrinkPreviewAdapter()
    private val mostPopularAdapter = DrinkPreviewAdapter()
    private val recentSearchesAdapter = DrinkPreviewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeInputActions(latestArrivalsAdapter)
        observeInputActions(mostPopularAdapter)
        observeInputActions(recentSearchesAdapter)
    }

    private fun observeInputActions(adapter: DrinkPreviewAdapter) {
        adapter
            .inputActions
            .filterIsInstance<InputActions.Click<DrinkPreviewUiModel>>()
            .onEach { onDrinkPreviewClicked(it.data) }
            .launchIn(lifecycleScope)

        adapter
            .inputActions
            .filterIsInstance<InputActions.LongClick<DrinkPreviewUiModel>>()
            .onEach { onDrinkPreviewLongClicked(it.data) }
            .launchIn(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeLandingPage()
    }

    private fun setupAdapters() {
        val divider = SpacerItemDecoration(right = 16.dpToPx().toInt())
        initDrinkPreviewRecyclerView(binding.latestArrivalsRv, latestArrivalsAdapter, divider)
        initDrinkPreviewRecyclerView(binding.mostPopularRv, mostPopularAdapter, divider)
        initDrinkPreviewRecyclerView(binding.recentSearchesRv, recentSearchesAdapter, divider)
    }

    private fun initDrinkPreviewRecyclerView(
        recyclerView: RecyclerView,
        adapter: DrinkPreviewAdapter,
        itemDecoration: RecyclerView.ItemDecoration
    ) {
        recyclerView.run {
            this.adapter = adapter
            addItemDecoration(itemDecoration)
        }
    }

    private fun observeLandingPage() {
        landingPageViewModel
            .landingPageLiveData
            .observe(viewLifecycleOwner, { onLandingPageReceived(it) })
    }

    private fun onLandingPageReceived(landingPage: LandingPageUiModel) {
        landingPage.run {
            latestArrivalsAdapter.update(latestArrivals)
            mostPopularAdapter.update(mostPopular)
            recentSearchesAdapter.updateWithAction(recentlyViewed) {
                binding.recentSearchesHeader.visibility = View.VISIBLE
            }
        }
    }

    private fun DrinkPreviewAdapter.updateWithAction(
        data: List<DrinkPreviewUiModel>,
        action: () -> Unit
    ) {
        update(data)
        if (data.isNotEmpty()) {
            action()
        }
    }

    private fun onDrinkPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreviewUiModel, ScreenNames.LANDING_PAGE)
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkPreviewLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreviewUiModel, ScreenNames.LANDING_PAGE)
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
    }
}
