package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewGridAdapter
import com.zemingo.drinksmenu.ui.view_model.WatchlistViewModel
import kotlinx.android.synthetic.main.fragment_watchlist.*
import org.koin.android.viewmodel.ext.android.viewModel

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private val watchlistViewModel: WatchlistViewModel by viewModel()
    private val previewAdapter = DrinkPreviewGridAdapter()

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