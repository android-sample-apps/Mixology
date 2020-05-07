package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewGridAdapter
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.view_model.AdvancedSearchViewModel
import kotlinx.android.synthetic.main.fragment_advanced_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AdvancedSearchFragment : Fragment(R.layout.fragment_advanced_search) {

    private val advancedSearchViewModel: AdvancedSearchViewModel by viewModel()

    private val query: String get() = search_query_et.text?.toString() ?: ""

    private val drinkPreviewAdapter = DrinkPreviewGridAdapter()
        .apply {
            onClick = { onDrinkClicked(it) }
        }

    private fun onDrinkClicked(drinkPreview: DrinkPreviewUiModel) {
        findNavController().navigate(
            AdvancedSearchFragmentDirections.actionAdvancedSearchFragmentToDrinkFragment(
                drinkPreview.id
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchQuery()
        initResultsRecyclerView()
        observeResults()
    }

    private fun initSearchQuery() {
        search_query_et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
                SpacerItemDecoration(
                    right = 4.dpToPx().toInt(),
                    left = 4.dpToPx().toInt(),
                    bottom = 4.dpToPx().toInt()
                )
            )
        }
    }

    private fun runSearchQuery() {
        advancedSearchViewModel.searchByName(query)
    }

    private fun observeResults() {
        advancedSearchViewModel
            .resultsLiveData
            .observe(viewLifecycleOwner, Observer {
                Timber.d("results: $it")
                drinkPreviewAdapter.update(it)
            })
    }
}