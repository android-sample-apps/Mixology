package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.extensions.hideKeyboard
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewGridAdapter
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.utils.InputActions
import com.zemingo.drinksmenu.ui.view_model.AdvancedSearchViewModel
import kotlinx.android.synthetic.main.fragment_advanced_search.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AdvancedSearchFragment : Fragment(R.layout.fragment_advanced_search) {

    private val advancedSearchViewModel: AdvancedSearchViewModel by viewModel()

    private val query: String get() = search_query_et.text?.toString() ?: ""

    private val drinkPreviewAdapter = DrinkPreviewGridAdapter()

    private fun onDrinkLongClicked(drinkPreview: DrinkPreviewUiModel) {
        Timber.d("onLongClicked: $drinkPreview")
        DrinkPreviewOptionsBottomFragment(drinkPreview).show(
            childFragmentManager,
            DrinkPreviewOptionsBottomFragment.TAG
        )
    }

    private fun onDrinkClicked(drinkPreview: DrinkPreviewUiModel) {
        requireView().hideKeyboard()
        findNavController().navigate(
            AdvancedSearchFragmentDirections
                .actionAdvancedSearchFragmentToDrinkFragment(drinkPreview)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            drinkPreviewAdapter.inputActions.collect {
                when (it) {
                    is InputActions.Click -> onDrinkClicked(it.data)
                    is InputActions.LongClick -> onDrinkLongClicked(it.data)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilterFab()
        initSearchQuery()
        initResultsRecyclerView()
        observeResults()
        observeSelectedFilters()
    }

    private fun initFilterFab() {
        filter_mfab.setOnClickListener {
            FilterBottomDialogFragment().show(childFragmentManager, "FilterDialog")
        }
    }

    private fun initSearchQuery() {
        search_container_til.setEndIconOnClickListener {
            hideNoResults()
            clearQuery()
        }

        search_query_et.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.hideKeyboard()
                runSearchQuery()
                true
            } else {
                false
            }
        }
    }

    private fun initResultsRecyclerView() {
        search_results_rv.run {
            adapter = drinkPreviewAdapter
            addItemDecoration(
                GridSpacerItemDecoration(
                    right = 4.dpToPx().toInt(),
                    left = 4.dpToPx().toInt(),
                    bottom = 4.dpToPx().toInt()
                )
            )
        }
    }

    private fun clearQuery() {
        advancedSearchViewModel.clearByName()
        search_query_et.text = null
        drinkPreviewAdapter.clear()
    }

    private fun runSearchQuery() {
        hideNoResults()
        advancedSearchViewModel.searchByName(query)
    }

    private fun observeResults() {
        advancedSearchViewModel
            .resultsLiveData
            .observe(viewLifecycleOwner, Observer {
                onResultsReceived(it)
            })
    }

    private fun observeSelectedFilters() {
        advancedSearchViewModel
            .searchFiltersLiveData
            .observe(viewLifecycleOwner, Observer {
                filter_mfab.run {
                    text = getString(R.string.filter_selected, it.activeFiltersBadge)
                    textVisibility = it.activeFiltersBadge != null
                }

                /*filter_search_fab.run {
//                    filter_search_fab.text = getString(R.string.filter_selected, it.activeFiltersBadge)
                    filter_search_fab.text =  it.activeFiltersBadge
                }*/

            })
    }

    private fun onResultsReceived(drinks: List<DrinkPreviewUiModel>) {
        Timber.d("results: received ${drinks.size} drinks")
        drinkPreviewAdapter.update(drinks)
        if (drinks.isEmpty()) {
            showNoResults()
        } else {
            hideNoResults()
        }
    }

    private fun showNoResults() {
        search_container_til.helperText = getString(R.string.no_results)
    }

    private fun hideNoResults() {
        search_container_til.helperText = null
    }
}