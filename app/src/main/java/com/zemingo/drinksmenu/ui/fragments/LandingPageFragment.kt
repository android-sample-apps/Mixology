package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.LandingPageUiModel
import com.zemingo.drinksmenu.ui.view_model.LandingPageViewModel
import kotlinx.android.synthetic.main.fragment_landing_page.*
import org.koin.android.viewmodel.ext.android.viewModel

class LandingPageFragment : Fragment(R.layout.fragment_landing_page) {

    private val landingPageViewModel: LandingPageViewModel by viewModel()
    private val latestArrivalsAdapter = DrinkPreviewAdapter()
    private val mostPopularAdapter = DrinkPreviewAdapter()
    private val recentSearchesAdapter = DrinkPreviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeLandingPage()
    }

    private fun setupAdapters() {
        val divider = SpacerItemDecoration(right = 16.dpToPx().toInt())
        initDrinkPreviewRecyclerView(latest_arrivals_rv, latestArrivalsAdapter, divider)
        initDrinkPreviewRecyclerView(most_popular_rv, mostPopularAdapter, divider)
        initDrinkPreviewRecyclerView(recent_searches_rv, recentSearchesAdapter, divider)
    }

    private fun initDrinkPreviewRecyclerView(
        recyclerView: RecyclerView,
        adapter: DrinkPreviewAdapter,
        itemDecoration: RecyclerView.ItemDecoration
    ) {
        recyclerView.run {
            this.adapter = adapter.apply { onClick = { onDrinkPreviewClicked(it) } }
            addItemDecoration(itemDecoration)
        }
    }

    private fun observeLandingPage() {
        landingPageViewModel
            .landingPageLiveData
            .observe(viewLifecycleOwner, Observer { onLandingPageReceived(it) })
    }

    private fun onLandingPageReceived(landingPage: LandingPageUiModel) {
        landingPage.run {
            latestArrivalsAdapter.update(latestArrivals)
            mostPopularAdapter.update(mostPopular)
            recentSearchesAdapter.update(recentSearches)
        }
    }

    private fun onDrinkPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        /*findNavController().navigate(
            LandingPageFragmentDirections
                .actionLandingPageFragmentToDrinkFragment(drinkPreviewUiModel.id)
        )*/

        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel.id)
        )
    }
}