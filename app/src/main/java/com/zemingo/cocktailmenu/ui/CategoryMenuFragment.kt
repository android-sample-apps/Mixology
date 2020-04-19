package com.zemingo.cocktailmenu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.cocktailmenu.R
import com.zemingo.cocktailmenu.models.CategoryUiModel
import com.zemingo.cocktailmenu.ui.adapters.DiffAdapter
import com.zemingo.cocktailmenu.view_model.CategoriesViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_category_menu.*
import kotlinx.android.synthetic.main.list_item_category.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryMenuFragment : Fragment(R.layout.fragment_category_menu) {

    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryAdapter = CategoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoriesMenu()
        observeCategories()
    }

    private fun initCategoriesMenu() {
        category_menu_rv.adapter = categoryAdapter
    }

    private fun observeCategories() {
        categoriesViewModel
            .categories
            .observe(viewLifecycleOwner, Observer { categoryAdapter.update(it) })
    }
}

private class CategoryAdapter : DiffAdapter<CategoryUiModel, CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(data: CategoryUiModel) {
            containerView.apply {
                category_name_tv.text = data.name
            }
        }
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        data: CategoryUiModel,
        position: Int
    ) {
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_category, parent, false)
        )
    }
}