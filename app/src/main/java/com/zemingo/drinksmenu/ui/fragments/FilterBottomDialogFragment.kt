package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.GridSpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_search_filters.*
import kotlinx.android.synthetic.main.layout_search_filters_test.*
import kotlinx.android.synthetic.main.list_item_selectable_filter.view.*

class FilterBottomDialogFragment : BottomSheetDialogFragment() {

    private val selectableAdapter = SelectableAdapter()

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
        filter_accept_btn.setOnClickListener {
            dismiss()
        }
        selected_btn.setOnClickListener {
            selected_filter_ml.transitionToEnd()
        }
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
            layoutManager = StaggeredGridLayoutManager(3, 1)
            adapter = selectableAdapter
            val data = mutableListOf<String>()
            for (i in 0 .. 15) {
                if (i.rem(2) == 0) {
                    data.add("Category $i")
                }
                else {
                    data.add("Category $i. this is uneven!")
                }
            }

            selectableAdapter.update(data)

        }
    }
}

class SelectableAdapter : DiffAdapter<String, SelectableAdapter.SelectableViewHolder>() {

    inner class SelectableViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(filter: String, position: Int) {

            containerView.run {
                filter_btn.text = filter
                if (position == 0) {
                    elevation = 4.dpToPx()
                }
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