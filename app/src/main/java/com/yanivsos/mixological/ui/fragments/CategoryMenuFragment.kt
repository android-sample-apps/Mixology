package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.CategoryAdapter
import com.yanivsos.mixological.ui.adapters.DrinkPreviewGridAdapter
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.utils.MyTransitionListener
import com.yanivsos.mixological.ui.view_model.CategoriesViewModel
import kotlinx.android.synthetic.main.fragment_category_menu.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryMenuFragment : Fragment(R.layout.fragment_category_menu) {

    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryAdapter = CategoryAdapter()
    private val drinkPreviewAdapter = DrinkPreviewGridAdapter()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerCategoryClicks()
        observeDrinkClicks()
        observeDrinkLongClicks()
    }

    private fun observeDrinkClicks() {
        lifecycleScope.launchWhenStarted {
            drinkPreviewAdapter
                .inputActions
                .filterIsInstance<InputActions.Click<DrinkPreviewUiModel>>()
                .collect {
                    onDrinkClicked(it.data)
                }
        }
    }

    private fun observeDrinkLongClicks() {
        lifecycleScope.launchWhenStarted {
            drinkPreviewAdapter
                .inputActions
                .filterIsInstance<InputActions.LongClick<DrinkPreviewUiModel>>()
                .collect {
                    onDrinkLongClicked(it.data)
                }
        }
    }

    private fun observerCategoryClicks() {
        lifecycleScope.launchWhenStarted {
            categoryAdapter
                .inputActions
                .filterIsInstance<InputActions.Click<CategoryUiModel>>()
                .collect { onCategoryClicked(it.data) }
        }
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
                .actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        Timber.d("onDrinkLongClicked: $drinkPreviewUiModel")
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
    }

    private fun onCategoryClicked(categoryUiModel: CategoryUiModel) {
        selected_title.text = categoryUiModel.name
        category_menu_ml.run {
            setTransitionListener(object : MyTransitionListener() {
                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    Timber.d("onTransitionCompleted: currentId[$currentId]")
                    onBackPressedCallback.isEnabled = currentId == R.id.results
                    if (currentId == R.id.results) {
                        categoriesViewModel.updateCategory(categoryUiModel.name)
                    }
                }
            })
            transitionToEnd()
        }
    }
}

