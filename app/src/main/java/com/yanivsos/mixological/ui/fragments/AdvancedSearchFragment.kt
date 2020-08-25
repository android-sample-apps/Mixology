package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.hideKeyboard
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.DrinkPreviewGridAdapter
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.SearchFiltersUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.utils.MyTransitionListener
import com.yanivsos.mixological.ui.view_model.AdvancedSearchViewModel
import com.yanivsos.mixological.ui.view_model.ConnectivityViewModel
import kotlinx.android.synthetic.main.fragment_advanced_search.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AdvancedSearchFragment : BaseFragment(R.layout.fragment_advanced_search) {

    private val advancedSearchViewModel: AdvancedSearchViewModel by viewModel()
    private val connectivityViewModel: ConnectivityViewModel by viewModel()

    private val query: String get() = search_query_actv.text?.toString() ?: ""

    private val drinkPreviewAdapter = DrinkPreviewGridAdapter()
    private val drinkAutoCompleteAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item)
    }

    private fun onDrinkLongClicked(drinkPreview: DrinkPreviewUiModel) {
        Timber.d("onLongClicked: $drinkPreview")
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreview, ScreenNames.SEARCH)
        DrinkPreviewOptionsBottomFragment(drinkPreview)
            .show(childFragmentManager)
    }

    private fun onDrinkClicked(drinkPreview: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreview, ScreenNames.SEARCH)
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

        lifecycleScope.launchWhenStarted {
            advancedSearchViewModel
                .autoCompleteSuggestions
                .collect { suggestions ->
                    Timber.d("suggestions: $suggestions")
                    val autoCompleteValues = suggestions.map { it.name }
                    drinkAutoCompleteAdapter.run {
                        clear()
                        addAll(autoCompleteValues)
                    }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayout()
        initFilterFab()
        initSearchQuery()
        initResultsRecyclerView()
        observeResults()
        observeSelectedFilters()
        observeConnectivity()
    }

    private fun initMotionLayout() {
        advanced_search_ml.setTransitionListener(object : MyTransitionListener() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                val isOnline = currentId == R.id.online
                Timber.d("filter button enabled: $isOnline")
                filter_image.run {
                    isClickable = isOnline
                    isFocusable = isOnline
                }
            }
        })
    }

    private fun initFilterFab() {
        filter_image.run {
            badgeTextFont =
                ResourcesCompat.getFont(requireContext(), R.font.jesa_script_regular)
            setOnClickListener {
                FilterBottomDialogFragment().show(childFragmentManager)
            }
        }
    }

    private fun initSearchQuery() {
        search_container_til.setEndIconOnClickListener {
            hideNoResults()
            clearQuery()
        }

        search_query_actv.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.hideKeyboard()
                runSearchQuery()
                true
            } else {
                false
            }
        }

        search_query_actv.setAdapter(drinkAutoCompleteAdapter)
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
        search_query_actv.text = null
        drinkPreviewAdapter.clear()
    }

    private fun runSearchQuery() {
        hideNoResults()
        advancedSearchViewModel.searchByName(query)
    }

    private fun observeResults() {
        advancedSearchViewModel
            .resultsLiveData
            .observe(viewLifecycleOwner, {
                onResultsReceived(it)
            })
    }

    private fun observeSelectedFilters() {
        advancedSearchViewModel
            .searchFiltersLiveData
            .observe(viewLifecycleOwner, {
                onSelectedFiltersReceived(it)
            })
    }

    private fun onSelectedFiltersReceived(searchFiltersUiModel: SearchFiltersUiModel) {
        filter_image.run {
            isShowCounter = searchFiltersUiModel.activeFiltersBadge != null
            badgeValue = searchFiltersUiModel.activeFiltersBadge ?: 0
        }
    }

    private fun observeConnectivity() {
        connectivityViewModel
            .connectivityLiveData
            .observe(viewLifecycleOwner, { isConnected ->
                onConnectivityChange(isConnected)
            })
    }

    private fun onConnectivityChange(isConnected: Boolean) {
        val transitionId = if (isConnected) R.id.online else R.id.offline
        advanced_search_ml.transitionToState(transitionId)
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