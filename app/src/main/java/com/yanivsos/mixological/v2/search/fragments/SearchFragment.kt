package com.yanivsos.mixological.v2.search.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentSearchBinding
import com.yanivsos.mixological.extensions.hideKeyboard
import com.yanivsos.mixological.ui.fragments.*
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.MyTransitionListener
import com.yanivsos.mixological.ui.view_model.ConnectivityViewModel
import com.yanivsos.mixological.v2.favorites.fragments.GridDrinkPreviewItem
import com.yanivsos.mixological.v2.search.viewModel.*
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val connectivityViewModel: ConnectivityViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    private val query: String get() = binding.searchQueryActv.text?.toString() ?: ""

    private val suggestionsAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            R.layout.list_item_autocomplete_drink
        )
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
        binding.advancedSearchMl.transitionToState(transitionId)
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
            SearchFragmentDirections
                .actionAdvancedSearchFragmentToDrinkFragment(drinkPreview)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayout()
        initFilterFab()
        initSearchQuery()
        observeAutoCompleteSuggestions()
        observeResults()
        observeFiltersBadge()
        observeFilterErrors()
        observeConnectivity()
    }

    private fun initMotionLayout() {
        binding.advancedSearchMl.setTransitionListener(object : MyTransitionListener() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                val searchEnabled = (currentId != R.id.offline)

                Timber.d("filter button enabled: $searchEnabled")
                binding.run {
                    filterImage.run {
                        isClickable = searchEnabled
                        isFocusable = searchEnabled

                    }
                    searchContainerTil.isEnabled = searchEnabled
                    if (!searchEnabled) {
                        searchContainerTil.hideKeyboard()
                    }
                }
            }
        })
    }

    private fun initFilterFab() {
        binding.filterImage.run {
            badgeTextFont =
                ResourcesCompat.getFont(requireContext(), R.font.jesa_script_regular)
            setOnClickListener {
                FilterBottomDialogFragment().show(childFragmentManager)
                binding.searchContainerTil.clearFocus()
            }
        }
    }

    private fun initSearchQuery() {
        Timber.d("initSearchQuery")
        binding.searchContainerTil.setEndIconOnClickListener {
            clearQuery()
        }
        binding.searchQueryActv.run {
            setOnEditorActionListener { _, actionId, _ ->
                (actionId == EditorInfo.IME_ACTION_SEARCH).also {
                    if (it) {
                        onRunSearchQuery()
                    }
                }
            }

            setAdapter(suggestionsAdapter)
            setOnItemClickListener { _, _, _, _ ->
                onRunSearchQuery()
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) dismissDropDown()
            }
        }
    }

    private fun clearQuery() {
        searchViewModel.clearByName()
        binding.searchQueryActv.text = null
    }

    private fun onRunSearchQuery() {
        binding.searchQueryActv.run {
            hideKeyboard()
            dismissDropDown()
        }
        search()
    }

    private fun search() {
        searchViewModel.fetchByName(query)
    }

    private fun observeResults() {
        searchViewModel
            .previewsState
            .withLifecycle()
            .onEach { onPreviewsStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun observeAutoCompleteSuggestions() {
        searchViewModel
            .suggestions
            .withLifecycle()
            .onEach { onSuggestionsStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun observeFilterErrors() {
        searchViewModel
            .filterErrors
            .withLifecycle()
            .onEach { filterError -> onFilterError(filterError) }
            .launchIn(viewLifecycleScope())
    }

    private fun onFilterError(filterError: FilterError) {
        when (filterError) {
            is FilterError.FetchByNameError -> Timber.d(
                filterError.throwable,
                "onFilterError: failed fetching ${filterError.name} "
            )
            is FilterError.ToggleFilterError -> Timber.d(
                filterError.throwable,
                "onFilterError: failed toggling filter ${filterError.filter}"
            )
        }.also {
            Toast.makeText(
                requireContext(),
                R.string.filter_toggle_failure,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onSuggestionsStateReceived(state: AutoCompleteSuggestionsState) {
        when (state) {
            AutoCompleteSuggestionsState.Empty -> onNoSuggestions()
            is AutoCompleteSuggestionsState.Found -> onFoundSuggestions(state)
        }
    }

    private fun onNoSuggestions() {
        Timber.d("onNoSuggestions")
        suggestionsAdapter.clear()
    }

    private fun onFoundSuggestions(state: AutoCompleteSuggestionsState.Found) {
        Timber.d("onFoundSuggestions: found ${state.suggestions.size} suggestions")
        suggestionsAdapter.run {
            clear()
            addAll(state.suggestions)
        }
    }

    private fun observeFiltersBadge() {
        searchViewModel
            .filterBadge
            .withLifecycle()
            .onEach { onFilterBadgeState(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onFilterBadgeState(filterBadgeState: FilterBadgeState) {
        Timber.d("onFilterBadgeState: $filterBadgeState")
        when (filterBadgeState) {
            is FilterBadgeState.Active -> showSelectedBadge(filterBadgeState)
            FilterBadgeState.None -> hideSelectedBadge()
        }
    }

    private fun hideSelectedBadge() {
        binding.filterImage.run {
            isShowCounter = false
            badgeValue = 0
        }
    }

    private fun showSelectedBadge(state: FilterBadgeState.Active) {
        binding.filterImage.run {
            isShowCounter = true
            badgeValue = state.count
        }
    }

    private suspend fun onPreviewsStateReceived(state: PreviewState) {
        when (state) {
            PreviewState.NoResults -> onPreviewNoResults()
            is PreviewState.Results -> onPreviewResults(state.previews)
            PreviewState.Loading -> Timber.d("onLoading previews")
        }
    }

    private fun onPreviewNoResults() {
        Timber.d("onPreviewNoResults:")
        binding.searchResults.updateNoResults()
    }

    private suspend fun onPreviewResults(drinks: List<DrinkPreviewUiModel>) {
        Timber.d("results: received ${drinks.size} drinks")
        createPreviewItems(drinks).also {
            binding.searchResults.updateResults(it)
        }
    }

    private suspend fun createPreviewItems(drinks: List<DrinkPreviewUiModel>): List<GridDrinkPreviewItem> {
        return withContext(Dispatchers.Default) {
            drinks.map {
                GridDrinkPreviewItem(
                    drinkPreviewUiModel = it,
                    onPreviewClicked = this@SearchFragment::onDrinkClicked,
                    onPreviewLongClicked = this@SearchFragment::onDrinkLongClicked
                )
            }
        }
    }
}
