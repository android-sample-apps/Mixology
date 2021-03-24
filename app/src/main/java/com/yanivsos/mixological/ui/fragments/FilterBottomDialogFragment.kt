package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.BottomDialogSearchFiltersBinding
import com.yanivsos.mixological.databinding.ListItemSelectableFilterBinding
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.ui.models.SearchFiltersUiModel
import com.yanivsos.mixological.ui.views.FilterHeaderView
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.mappers.toLongId
import com.yanivsos.mixological.v2.search.useCases.FilterModel
import com.yanivsos.mixological.v2.search.useCases.SelectedFilters
import com.yanivsos.mixological.v2.search.viewModel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

private const val TAG = "FilterBottomDialogFragment"

class FilterBottomDialogFragment : BaseBottomSheetDialogFragment() {

    private var binding: BottomDialogSearchFiltersBinding? = null

    private val searchViewModel: SearchViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    private val alcoholicAdapter = GroupieAdapter()
    private val ingredientsAdapter = GroupieAdapter()
    private val categoryAdapter = GroupieAdapter()
    private val glassAdapter = GroupieAdapter()

    private fun onFilterClicked(drinkFilter: DrinkFilter) {
        Timber.d("onFilterClicked: $drinkFilter")
        searchViewModel.toggleFilter(drinkFilter)
    }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        BottomDialogSearchFiltersBinding.inflate(inflater, container, false).run {
            binding = this
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeaderView()
        initFiltersRecyclerView()
//        initIngredientSearch()
        observerFilters()
    }

    private fun initHeaderView() {
        binding?.ingredientHeaderFhv?.onFilterClearedClickedListener = {
            searchViewModel.clearIngredientsFilter()
        }
        binding?.alcoholicHeaderFhv?.onFilterClearedClickedListener = {
            searchViewModel.clearAlcoholicFilter()
        }
        binding?.glassesHeaderFhv?.onFilterClearedClickedListener = {
            searchViewModel.clearGlassesFilter()
        }
        binding?.categoryHeaderFhv?.onFilterClearedClickedListener = {
            searchViewModel.clearCategoriesFilter()
        }
    }

    /*private fun initIngredientSearch() {
        binding?.ingredientSearchQueryEt?.addTextChangedListener {
            val name = it?.toString()
            advancedSearchViewModel.onIngredientNameSearch(name)
        }
        advancedSearchViewModel.onIngredientNameSearch(null)
    }*/

    private fun observerFilters() {
        searchViewModel
            .selectedFilters
            .onEach { onSelectedFiltersState(it) }
            .launchIn(viewLifecycleScope())
    }

    private suspend fun onSelectedFiltersState(selectedFilters: SelectedFilters) {
        selectedFilters.run {
            alcoholicAdapter.updateAsync(alcoholic.toItems {
                onFilterClicked(
                    DrinkFilter.Alcoholic(
                        it.name
                    )
                )
            })
            categoryAdapter.updateAsync(categories.toItems {
                onFilterClicked(
                    DrinkFilter.Category(
                        it.name
                    )
                )
            })
            ingredientsAdapter.updateAsync(ingredients.toItems {
                /*onFilterClicked(
                    DrinkFilter.Ingredients(
                        it.name
                    )
                )*/
                // TODO: 23/03/2021 complete this
            })
            glassAdapter.updateAsync(glasses.toItems {
                onFilterClicked(DrinkFilter.Glass(it.name))
            })
        }
    }

    private suspend fun List<FilterModel>.toItems(onFilterClicked: (FilterModel) -> Unit): List<FilterItem> {
        return withContext(Dispatchers.Default) {
            map {
                FilterItem(
                    filter = it,
                    onFilterClicked = onFilterClicked
                )
            }
        }
    }

    private fun updateResults(searchFiltersUiModel: SearchFiltersUiModel) {
        /*updateSelectableAdapter(alcoholicAdapter, searchFiltersUiModel.filters[FilterType.ALCOHOL])
        updateSelectableAdapter(categoryAdapter, searchFiltersUiModel.filters[FilterType.CATEGORY])
        updateSelectableAdapter(glassAdapter, searchFiltersUiModel.filters[FilterType.GLASS])
        updateSelectableAdapter(
            ingredientsAdapter,
            searchFiltersUiModel.filters[FilterType.INGREDIENTS]
        )*/
    }

    /*private fun updateActiveFilters(searchFiltersUiModel: SearchFiltersUiModel) {
        Timber.d("updateActiveFilters: ${searchFiltersUiModel.activeFilters}")
        binding?.run {
            updateFilterHeaders(
                alcoholicHeaderFhv,
                searchFiltersUiModel.activeFilters[FilterType.ALCOHOL]
            )
            updateFilterHeaders(
                categoryHeaderFhv,
                searchFiltersUiModel.activeFilters[FilterType.CATEGORY]
            )
            updateFilterHeaders(
                glassesHeaderFhv,
                searchFiltersUiModel.activeFilters[FilterType.GLASS]
            )

            updateFilterHeaders(
                ingredientHeaderFhv,
                searchFiltersUiModel.activeFilters[FilterType.INGREDIENTS]
            )
        }
    }*/

    private fun updateFilterHeaders(filterHeaderView: FilterHeaderView, count: Int) {
        filterHeaderView.run {
            filters = count.toString()
        }
    }

    /*private fun updateSelectableAdapter(
        selectableAdapter: SelectableAdapter,
        filters: List<DrinkFilterUiModel>?
    ) {
        selectableAdapter.submitList(filters ?: emptyList())
    }*/

    private fun initFiltersRecyclerView() {
        binding?.run {
            initRecyclerView(filterAlcoholicRv, alcoholicAdapter)
            initRecyclerView(filterIngredientsRv, ingredientsAdapter)
            initRecyclerView(filterCategoryRv, categoryAdapter)
            initRecyclerView(filterGlassesRv, glassAdapter)
        }
    }

    private fun initRecyclerView(
        recyclerView: RecyclerView,
        filterAdapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.run {
            val padding = 4.dpToPx().toInt()
            addItemDecoration(
                GridSpacerItemDecoration(
                    top = padding,
                    bottom = padding,
                    left = padding,
                    right = padding
                )
            )
            adapter = filterAdapter
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}

class FilterItem(
    private val filter: FilterModel,
    private val onFilterClicked: (FilterModel) -> Unit
) : BindableItem<ListItemSelectableFilterBinding>() {

    override fun bind(viewBinding: ListItemSelectableFilterBinding, position: Int) {
        viewBinding.root.run {
            setOnClickListener { onFilterClicked(filter) }

            val selectedColor = context.compatColor(getColor(filter.isSelected))
            viewBinding.cardContainer.run {
                strokeColor = selectedColor
                elevation = getElevation(filter.isSelected)
            }
            viewBinding.filterTv.run {
                setTextColor(selectedColor)
                text = filter.name
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.list_item_selectable_filter
    }

    override fun initializeViewBinding(view: View): ListItemSelectableFilterBinding {
        return ListItemSelectableFilterBinding.bind(view)
    }

    private fun getColor(selected: Boolean): Int {
        return if (selected) R.color.header_text_color else R.color.secondary_text_color_alpha_50
    }

    private fun getElevation(selected: Boolean): Float {
        return if (selected) 8.dpToPx() else 0.dpToPx()
    }

    override fun getId(): Long {
        return filter.name.toLongId()
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return when (other) {
            is FilterItem -> other.filter == this.filter
            else -> super.hasSameContentAs(other)
        }
    }
}
