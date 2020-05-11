package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.ui.view_model.AdvancedFiltersViewModel
import com.zemingo.drinksmenu.ui.view_model.AdvancedSearchViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_search_filters.*
import kotlinx.android.synthetic.main.list_item_selectable_filter.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FilterBottomDialogFragment : BottomSheetDialogFragment() {

    private val advancedFiltersViewModel: AdvancedFiltersViewModel by viewModel()
    private val advancedSearchViewModel: AdvancedSearchViewModel by sharedViewModel()
    private val selectableAdapter = SelectableAdapter().apply {
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
        initAlcoholicRecyclerView()
        observerFilters()
        filter_accept_btn.setOnClickListener {
            dismiss()
        }
    }

    private fun observerFilters() {
        advancedFiltersViewModel.searchFilters.observe(
            viewLifecycleOwner, Observer {
                Timber.d("Received filters: $it")
                selectableAdapter.update(it.alcoholic.map { it.name })
            }
        )
    }

    private fun initAlcoholicRecyclerView() {
        filter_category_rv.run {
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

class SelectableAdapter : DiffAdapter<String, SelectableAdapter.SelectableViewHolder>() {

    var onClicked: ((DrinkFilter) -> Unit)? = null

    inner class SelectableViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(filter: String, position: Int) {
            containerView.run {
                filter_btn.text = filter
                filter_btn.setOnClickListener { onClicked?.invoke(DrinkFilter(filter, FilterType.ALCOHOL)) }
            }
        }
    }

    override fun onBindViewHolder(holder: SelectableViewHolder, data: String, position: Int) {
        holder.bind(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        return SelectableViewHolder(
            parent.viewHolderInflate(R.layout.list_item_selectable_filter)
        )
    }
}