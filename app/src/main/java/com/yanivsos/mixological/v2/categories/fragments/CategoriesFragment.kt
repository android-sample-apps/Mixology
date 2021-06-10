package com.yanivsos.mixological.v2.categories.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentCategoryMenuBinding
import com.yanivsos.mixological.databinding.ListItemCategoryBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.DrinkPreviewOptionsBottomFragment
import com.yanivsos.mixological.ui.fragments.HomeFragmentDirections
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.SelectedCategoryUiModel
import com.yanivsos.mixological.ui.utils.TimberTransitionListener
import com.yanivsos.mixological.v2.categories.viewModel.CategoriesUiState
import com.yanivsos.mixological.v2.categories.viewModel.CategoriesViewModel
import com.yanivsos.mixological.v2.favorites.fragments.GridDrinkPreviewItem
import com.yanivsos.mixological.v2.mappers.toLongId
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoriesFragment : BaseFragment(R.layout.fragment_category_menu) {

    private val binding by viewBinding(FragmentCategoryMenuBinding::bind)
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryAdapter = GroupieAdapter()
    private val drinkPreviewAdapter = GroupieAdapter()

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.categoryMenuMl.transitionToStart()
        }
    }

    // TODO: 24/05/2021 research this warning
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        initCategoriesMenu()
        initDrinkPreviews()
        observeCategoriesState()
    }

    private fun initCategoriesMenu() {
        setSwipeTransitionEnabled(false)
        if (categoriesViewModel.isExpanded) {
            setExpanded()
        }

        categoriesViewModel
            .isExpandedFlow
            .withLifecycle()
            .onEach { isExpanded -> onExpandStateChanged(isExpanded) }
            .launchIn(viewLifecycleScope())

        binding.categoryMenuRv.run {
            adapter = categoryAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }

        binding.categoryMenuMl.addTransitionListener(CategoryTransitionListener { isExpanded ->
            categoriesViewModel.isExpanded = isExpanded
        })
    }

    private fun initDrinkPreviews() {
        binding.categoriesPreviewRv.run {
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

    private fun observeCategoriesState() {
        categoriesViewModel
            .categoriesState
            .withLifecycle()
            .onEach { onStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onExpandStateChanged(isExpanded: Boolean) {
        Timber.d("onExpandStateChanged: isExpanded[$isExpanded]")
        onBackPressedCallback.isEnabled = isExpanded
        if (isExpanded) {
            animateExpanded()
        } else {
            animateCollapsed()
        }
    }

    private suspend fun onStateReceived(state: CategoriesUiState) {
        when (state) {
            is CategoriesUiState.CategoryNotSelected -> onCategoryNotSelected(state)
            is CategoriesUiState.CategorySelected -> onCategorySelected(state)
            CategoriesUiState.Loading -> onLoadingState()
        }
    }

    private fun onLoadingState() {
        Timber.d("onLoading")
    }

    private suspend fun onCategorySelected(state: CategoriesUiState.CategorySelected) {
        Timber.d("onCategorySelected: $state")
        updateCategories(state.categories)
        updateDrinkPreviews(state.selectedCategoryUiModel)
        setSwipeTransitionEnabled(true)
    }

    private suspend fun onCategoryNotSelected(state: CategoriesUiState.CategoryNotSelected) {
        Timber.d("onCategoryNotSelected: $state")
        updateCategories(state.categories)
        updateDrinkPreviews()
    }

    private suspend fun updateDrinkPreviews(selectedCategory: SelectedCategoryUiModel? = null) {
        val pair = selectedCategory?.let { it.name to it.drinks } ?: null to emptyList()
        binding.selectedTitle.text = pair.first
        drinkPreviewAdapter.updateAsync(pair.second.mapToDrinkItems())
    }

    private suspend fun updateCategories(categories: List<CategoryUiModel>) {
        Timber.d("updateCategories: updating ${categories.size} categories")
        categoryAdapter.updateAsync(categories.mapToCategoryItems())
    }

    private suspend fun List<DrinkPreviewUiModel>.mapToDrinkItems(): List<GridDrinkPreviewItem> {
        return withContext(Dispatchers.Default) {
            map {
                GridDrinkPreviewItem(
                    drinkPreviewUiModel = it,
                    onPreviewLongClicked = this@CategoriesFragment::onDrinkLongClicked,
                    onPreviewClicked = this@CategoriesFragment::onDrinkClicked
                )
            }
        }
    }

    private suspend fun List<CategoryUiModel>.mapToCategoryItems(): List<CategoryItem> {
        return withContext(Dispatchers.Default) {
            map {
                CategoryItem(
                    categoryUiModel = it,
                    onClick = this@CategoriesFragment::onCategoryClicked
                )
            }
        }
    }

    private fun animateExpanded() {
        Timber.d("setExpanded")
        binding.categoryMenuMl.transitionToState(R.id.results)
    }

    private fun setExpanded() {
        binding.categoryMenuMl.progress = 1.0f
    }

    private fun animateCollapsed() {
        Timber.d("setCollapsed")
        binding.categoryMenuMl.transitionToState(R.id.only_categories)
    }

    private fun setSwipeTransitionEnabled(isEnabled: Boolean) {
        binding.categoryMenuMl.getTransition(R.id.swipe_transition).setEnable(isEnabled)
    }

    private fun onDrinkClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        Timber.d("onDrinkClicked: $drinkPreviewUiModel")
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreviewUiModel, ScreenNames.CATEGORIES)
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        Timber.d("onDrinkLongClicked: $drinkPreviewUiModel")
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreviewUiModel, ScreenNames.CATEGORIES)
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
    }

    private fun onCategoryClicked(categoryUiModel: CategoryUiModel) {
        updateSelectedCategory (categoryUiModel)
    }

    private fun updateSelectedCategory(categoryUiModel: CategoryUiModel) {
        binding.selectedTitle.text = categoryUiModel.name
        categoriesViewModel.updateSelected(categoryUiModel)
        categoriesViewModel.isExpanded = true
    }
}

private class CategoryItem(
    private val categoryUiModel: CategoryUiModel,
    private val onClick: (CategoryUiModel) -> Unit
) : BindableItem<ListItemCategoryBinding>() {

    override fun bind(viewBinding: ListItemCategoryBinding, position: Int) {
        viewBinding.root.setOnClickListener { onClick(categoryUiModel) }
        viewBinding.categoryNameTv.text = categoryUiModel.name
    }

    override fun getLayout(): Int {
        return R.layout.list_item_category
    }

    override fun initializeViewBinding(view: View): ListItemCategoryBinding {
        return ListItemCategoryBinding.bind(view)
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return when (other) {
            is CategoryItem -> other.categoryUiModel == this.categoryUiModel
            else -> super.hasSameContentAs(other)
        }
    }

    override fun getId(): Long {
        return categoryUiModel.name.toLongId()
    }
}

private class CategoryTransitionListener(
    private val onExpand: (Boolean) -> Unit
) : TimberTransitionListener() {

    init {
        Timber.d("init: hashcode: ${hashCode()}")
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
        super.onTransitionCompleted(motionLayout, currentId)
        onExpand(currentId != R.id.only_categories)
    }
}
