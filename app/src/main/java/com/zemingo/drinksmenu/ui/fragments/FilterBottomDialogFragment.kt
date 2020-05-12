package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.extensions.compatColor
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.ui.models.DrinkFilterUiModel
import com.zemingo.drinksmenu.ui.view_model.AdvancedFiltersViewModel
import com.zemingo.drinksmenu.ui.view_model.AdvancedSearchViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_search_filters.*
import kotlinx.android.synthetic.main.list_item_selectable_filter.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FilterBottomDialogFragment : BottomSheetDialogFragment() {

    private val advancedFiltersViewModel: AdvancedFiltersViewModel by viewModel()
    private val advancedSearchViewModel: AdvancedSearchViewModel by viewModel()
    private val alcoholicAdapter = SelectableAdapter().apply {
        onClicked = {
            advancedSearchViewModel.updateFilter(it)
        }
    }

    private val ingredientsAdapter = SelectableAdapter().apply {
        onClicked = {
            advancedSearchViewModel.updateFilter(it)
        }
    }

    private val categoryAdapter = SelectableAdapter().apply {
        onClicked = {
            advancedSearchViewModel.updateFilter(it)
        }
    }

    private val glassAdapter = SelectableAdapter().apply {
        onClicked = {
            advancedSearchViewModel.updateFilter(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_search_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFiltersRecyclerView()
        observerFilters()
        filter_accept_btn.setOnClickListener {
            dismiss()
        }
    }

    private fun observerFilters() {
        advancedFiltersViewModel.searchFilters.observe(
            viewLifecycleOwner, Observer {
                Timber.d("Received filters:")
                updateSelectableAdapter(alcoholicAdapter, it.filters[FilterType.ALCOHOL])
                updateSelectableAdapter(categoryAdapter, it.filters[FilterType.CATEGORY])
                updateSelectableAdapter(glassAdapter, it.filters[FilterType.GLASS])
                updateSelectableAdapter(ingredientsAdapter, it.filters[FilterType.INGREDIENTS])
            }
        )
    }

    private fun updateSelectableAdapter(
        selectableAdapter: SelectableAdapter,
        filters: List<DrinkFilterUiModel>?
    ) {
        selectableAdapter.set(filters ?: emptyList())
//        selectableAdapter.update(filters ?: emptyList())
    }

    private fun initFiltersRecyclerView() {
        initRecyclerView(filter_alcoholic_rv, alcoholicAdapter)
        initRecyclerView(filter_ingredients_rv, ingredientsAdapter)
        initRecyclerView(filter_category_rv, categoryAdapter)
        initRecyclerView(filter_glasses_rv, glassAdapter)
    }

    private fun initRecyclerView(recyclerView: RecyclerView, selectableAdapter: SelectableAdapter) {
        recyclerView.run {
            val padding = 8.dpToPx().toInt()
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
}


class SelectableAdapter :
    DiffAdapter<DrinkFilterUiModel, SelectableAdapter.SelectableViewHolder>() {

    var onClicked: ((DrinkFilter) -> Unit)? = null

    inner class SelectableViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        //todo - fix shitty code
        fun bind(filter: DrinkFilterUiModel) {
            containerView.run {
                setOnClickListener { invokeFilterClicked(filter) }

                val selectedColor = context.compatColor(filter.color)
                card_container.run {
                    strokeColor = selectedColor
                    elevation = filter.elevation
                    alpha = filter.alpha
                }
                filter_tv.run {
                    setTextColor(selectedColor)
                    text = filter.name
                }
            }
        }
    }

    private fun invokeFilterClicked(filter: DrinkFilterUiModel) {
        onClicked?.invoke(filter.drinkFilter.copy(active = !filter.selected))
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