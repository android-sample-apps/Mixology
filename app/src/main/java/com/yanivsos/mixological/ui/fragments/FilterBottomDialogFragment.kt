package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.R
import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.FilterType
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.viewHolderInflate
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.DiffAdapter
import com.yanivsos.mixological.ui.models.DrinkFilterUiModel
import com.yanivsos.mixological.ui.models.SearchFiltersUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.view_model.AdvancedSearchViewModel
import com.yanivsos.mixological.ui.views.FilterHeaderView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.bottom_dialog_search_filters.*
import kotlinx.android.synthetic.main.list_item_selectable_filter.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

private const val TAG = "FilterBottomDialogFragment"

class FilterBottomDialogFragment : BaseBottomSheetDialogFragment() {

    private val advancedSearchViewModel: AdvancedSearchViewModel by lazy {
        @Suppress("RemoveExplicitTypeArguments")
        requireParentFragment().getViewModel<AdvancedSearchViewModel>()
    }
    private val alcoholicAdapter = SelectableAdapter()
    private val ingredientsAdapter = SelectableAdapter()
    private val categoryAdapter = SelectableAdapter()
    private val glassAdapter = SelectableAdapter()

    private fun onFilterClicked(drinkFilter: DrinkFilter) {
        Timber.d("onFilterClicked: $drinkFilter")
        advancedSearchViewModel.updateFilter(drinkFilter)
    }

    private fun observeInputActions(adapter: SelectableAdapter) {
        lifecycleScope.launchWhenStarted {
            adapter.inputActions.filterIsInstance<InputActions.Click<DrinkFilterUiModel>>()
                .map { it.data.drinkFilter }
                .collect {
                    onFilterClicked(it)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeInputActions(alcoholicAdapter)
        observeInputActions(ingredientsAdapter)
        observeInputActions(categoryAdapter)
        observeInputActions(glassAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_search_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFiltersRecyclerView()
        initIngredientSearch()
        observerFilters()
    }

    private fun initIngredientSearch() {
        ingredient_search_query_et.addTextChangedListener {
            val name = it?.toString()
            advancedSearchViewModel.onIngredientNameSearch(name)
        }
        advancedSearchViewModel.onIngredientNameSearch(null)
    }

    private fun observerFilters() {
        advancedSearchViewModel.searchFiltersLiveData.observe(
            viewLifecycleOwner, Observer {
                Timber.d("Received filters: ${it.activeFilters}")
                updateResults(it)
                updateActiveFilters(it)
            }
        )
    }

    private fun updateResults(searchFiltersUiModel: SearchFiltersUiModel) {
        updateSelectableAdapter(alcoholicAdapter, searchFiltersUiModel.filters[FilterType.ALCOHOL])
        updateSelectableAdapter(categoryAdapter, searchFiltersUiModel.filters[FilterType.CATEGORY])
        updateSelectableAdapter(glassAdapter, searchFiltersUiModel.filters[FilterType.GLASS])
        updateSelectableAdapter(
            ingredientsAdapter,
            searchFiltersUiModel.filters[FilterType.INGREDIENTS]
        )
    }

    private fun updateActiveFilters(searchFiltersUiModel: SearchFiltersUiModel) {
        Timber.d("updateActiveFilters: ${searchFiltersUiModel.activeFilters}")
        updateFilterHeaders(
            alcoholic_header_tv,
            searchFiltersUiModel.activeFilters[FilterType.ALCOHOL]
        )
        updateFilterHeaders(
            category_header_tv,
            searchFiltersUiModel.activeFilters[FilterType.CATEGORY]
        )
        updateFilterHeaders(
            glasses_header_tv,
            searchFiltersUiModel.activeFilters[FilterType.GLASS]
        )

        updateFilterHeaders(
            ingredient_header_tv,
            searchFiltersUiModel.activeFilters[FilterType.INGREDIENTS]
        )
    }

    private fun updateFilterHeaders(filterHeaderView: FilterHeaderView, activeFilters: Int?) {
        filterHeaderView.run {
            filters = activeFilters?.toString()
        }
    }

    private fun updateSelectableAdapter(
        selectableAdapter: SelectableAdapter,
        filters: List<DrinkFilterUiModel>?
    ) {
        selectableAdapter.set(filters ?: emptyList())
    }

    private fun initFiltersRecyclerView() {
        initRecyclerView(filter_alcoholic_rv, alcoholicAdapter)
        initRecyclerView(filter_ingredients_rv, ingredientsAdapter)
        initRecyclerView(filter_category_rv, categoryAdapter)
        initRecyclerView(filter_glasses_rv, glassAdapter)
    }

    private fun initRecyclerView(recyclerView: RecyclerView, selectableAdapter: SelectableAdapter) {
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
            adapter = selectableAdapter
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}


class SelectableAdapter :
    DiffAdapter<DrinkFilterUiModel, SelectableAdapter.SelectableViewHolder>() {

    override fun calculateDiff(
        old: List<DrinkFilterUiModel>,
        new: List<DrinkFilterUiModel>
    ): DiffUtil.DiffResult {
        val diffCallback = DrinkFilterDiffCallback(old, new)
        return DiffUtil.calculateDiff(diffCallback)
    }

    inner class SelectableViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(filter: DrinkFilterUiModel) {
            containerView.run {
                setOnClickListener { invokeFilterClicked(filter) }

                val selectedColor = context.compatColor(getColor(filter.selected))
                card_container.run {
                    strokeColor = selectedColor
                    elevation = getElevation(filter.selected)
                    alpha = getAlpha(filter.selected)
                }
                filter_tv.run {
                    setTextColor(selectedColor)
                    text = filter.name
                }
            }
        }
    }

    private fun getColor(selected: Boolean): Int {
        return if (selected) R.color.header_text_color else R.color.secondary_text_color
    }

    private fun getAlpha(selected: Boolean): Float {
        return if (selected) 1f else 0.5f
    }

    private fun getElevation(selected: Boolean): Float {
        return if (selected) 8.dpToPx() else 0.dpToPx()
    }

    private fun invokeFilterClicked(filter: DrinkFilterUiModel) {
        val drinkFilter = filter.drinkFilter.copy(active = !filter.selected)
        sendInputAction(InputActions.Click(filter.copy(drinkFilter = drinkFilter)))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        return SelectableViewHolder(
            parent.viewHolderInflate(R.layout.list_item_selectable_filter)
        )
    }

    override fun onBindViewHolder(
        holder: SelectableViewHolder,
        data: DrinkFilterUiModel,
        position: Int
    ) {
        holder.bind(data)
    }
}

private class DrinkFilterDiffCallback(
    private val oldList: List<DrinkFilterUiModel>,
    private val newList: List<DrinkFilterUiModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].drinkFilter.query == newList[newItemPosition].drinkFilter.query
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].selected == newList[newItemPosition].selected
    }


}