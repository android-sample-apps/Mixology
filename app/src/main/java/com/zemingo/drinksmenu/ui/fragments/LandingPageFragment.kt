package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
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
        latest_arrivals_rv.adapter = latestArrivalsAdapter
        latest_arrivals_rv.addItemDecoration(divider)
        most_popular_rv.adapter = mostPopularAdapter
        most_popular_rv.addItemDecoration(divider)
        recent_searches_rv.adapter = recentSearchesAdapter
        recent_searches_rv.addItemDecoration(divider)
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
}