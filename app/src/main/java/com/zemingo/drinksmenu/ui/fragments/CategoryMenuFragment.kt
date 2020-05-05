package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.CategoryAdapter
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewGridAdapter
import com.zemingo.drinksmenu.ui.models.CategoryUiModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.utils.MyTransitionListener
import com.zemingo.drinksmenu.ui.view_model.CategoriesViewModel
import kotlinx.android.synthetic.main.fragment_category_menu.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryMenuFragment : Fragment(R.layout.fragment_category_menu) {

    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryAdapter =
        CategoryAdapter().apply {
            onClick = { onCategoryClicked(it) }
        }

    private val drinkPreviewAdapter = DrinkPreviewGridAdapter()
        .apply {
            onClick = { onDrinkClicked(it) }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            category_menu_ml.transitionToStart()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoriesMenu()
        initDrinkPreviews()
        observeCategories()
        observeDrinkPreviews()
    }

    private fun initCategoriesMenu() {
        onBackPressedCallback.isEnabled = false
        category_menu_rv.run {
            adapter = categoryAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }
    }

    private fun initDrinkPreviews() {
        categories_preview_rv.run {
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

    private fun observeCategories() {
        categoriesViewModel
            .categories
            .observe(viewLifecycleOwner, Observer {
                categoryAdapter.update(it)
            })
    }

    private fun observeDrinkPreviews() {
        categoriesViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer {
                drinkPreviewAdapter.update(it)
            })
    }

    private fun onDrinkClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        Timber.d("onDrinkClicked: $drinkPreviewUiModel")
            findNavController().navigate(
                HomeFragmentDirections
                    .actionHomeFragmentToDrinkFragment(drinkPreviewUiModel.id)
            )
    }


    private fun onCategoryClicked(categoryUiModel: CategoryUiModel) {
        selected_title.text = categoryUiModel.name
        category_menu_ml.run {

            setTransitionListener(object : MyTransitionListener() {

                override fun onTransitionStarted(
                    motionLayout: MotionLayout,
                    startId: Int,
                    endId: Int
                ) {
                    onBackPressedCallback.isEnabled = endId == R.id.results
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    Timber.d("onTransitionCompleted: currentId[$currentId]")
                    if (currentId == R.id.results) {
                        categoriesViewModel.updateCategory(categoryUiModel.name)
                    }
                }
            })
            transitionToEnd()
        }
    }
}

