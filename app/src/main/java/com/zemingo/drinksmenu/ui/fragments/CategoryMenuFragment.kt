package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.viewHolderInflate
import com.zemingo.drinksmenu.ui.adapters.DiffAdapter
import com.zemingo.drinksmenu.ui.models.CategoryUiModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.view_model.CategoriesViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_category_menu.*
import kotlinx.android.synthetic.main.list_item_category.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryMenuFragment : Fragment(R.layout.fragment_category_menu) {

    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryAdapter =
        CategoryAdapter().apply {
            onClick = { onCategoryClicked(it) }
        }

    private val drinkPreviewAdapter = DrinkPreviewAdapter()
        .apply {
            onClick = { onDrinkClicked(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoriesMenu()
        observeCategories()
        observeDrinkPreviews()
    }

    private fun initCategoriesMenu() {
        category_menu_rv.run {
            adapter = categoryAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }

        categories_preview_rv.adapter = drinkPreviewAdapter
    }

    private fun observeCategories() {
        categoriesViewModel
            .categories
            .observe(viewLifecycleOwner, Observer { categoryAdapter.update(it) })
    }

    private fun observeDrinkPreviews() {
        categoriesViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer { drinkPreviewAdapter.update(it) })
    }

    private fun onDrinkClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToDrinkFragment(drinkPreviewUiModel.id)
        )
    }

    private fun onCategoryClicked(categoryUiModel: CategoryUiModel) {
        selected_title.text = categoryUiModel.name
        category_menu_ml.run {
            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    Timber.d("onTransitionStarted: p1[$p1] p2[$p2]")
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//                    Timber.d("onTransitionChange: p1[$p1], p2[$p2], p3[$p3]")
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    Timber.d("onTransitionCompleted: p1[$p1]")
                    setTransitionListener(null)
                    categoriesViewModel.updateCategory(categoryUiModel.name)
                }
            })
            transitionToEnd()
        }
    }

    private class CategoryAdapter :
        DiffAdapter<CategoryUiModel, CategoryAdapter.CategoryViewHolder>() {

        var onClick: ((CategoryUiModel) -> Unit)? = null

        inner class CategoryViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

            fun bind(data: CategoryUiModel) {
                containerView.apply {
                    setOnClickListener { onClick?.invoke(data) }
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
                parent.viewHolderInflate(R.layout.list_item_category)
            )
        }
    }
}