package com.yanivsos.mixological.v2.drink.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentIngredientsBinding
import com.yanivsos.mixological.databinding.ListItemIngredientBinding
import com.yanivsos.mixological.databinding.ListItemIngredientLoadingBinding
import com.yanivsos.mixological.extensions.toBundle
import com.yanivsos.mixological.extensions.toDrinkPreviewUiModel
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.IngredientBottomSheetDialogFragment
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import com.yanivsos.mixological.ui.models.quantityVisibility
import com.yanivsos.mixological.v2.drink.EmptyBindableItem
import com.yanivsos.mixological.v2.drink.viewModel.DrinkViewModel
import com.yanivsos.mixological.v2.drink.viewModel.IngredientsState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class IngredientsFragment : BaseFragment(R.layout.fragment_ingredients) {

    companion object {
        fun newInstance(drinkPreviewUiModel: DrinkPreviewUiModel): IngredientsFragment {
            return IngredientsFragment().apply {
                arguments = drinkPreviewUiModel.toBundle()
            }
        }
    }

    private val binding by viewBinding(FragmentIngredientsBinding::bind)
    private val ingredientAdapter = GroupieAdapter()
    private val drinkPreviewUiModel: DrinkPreviewUiModel by lazy {
        requireArguments().toDrinkPreviewUiModel()!!
    }

    private val viewModel: DrinkViewModel by lazy {
        requireParentFragment().getViewModel { parametersOf(drinkPreviewUiModel.id) }
    }

    private fun onIngredientLongClicked(ingredientUiModel: IngredientUiModel) {
        AnalyticsDispatcher.onIngredientLongClicked(ingredientUiModel, ScreenNames.INGREDIENTS)
        IngredientBottomSheetDialogFragment(ingredientUiModel).show(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIngredientsRecyclerView()
        observeIngredients()
    }

    private fun initIngredientsRecyclerView() {
        binding.ingredientsRv.run {
            adapter = ingredientAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }
    }

    private fun observeIngredients() {
        viewModel
            .ingredients
            .onEach { onIngredientsStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onIngredientsStateReceived(state: IngredientsState) {
        when (state) {
            is IngredientsState.Loading -> onLoadingState(state)
            is IngredientsState.Success -> onSuccessState(state)
            is IngredientsState.Error -> onErrorState(state)
        }
    }

    private fun onSuccessState(state: IngredientsState.Success) {
        Timber.d("onSuccessState: $state")
        ingredientAdapter
            .updateAsync(
                createIngredientItems(state.ingredients)
            )
    }

    private fun onLoadingState(state: IngredientsState.Loading) {
        Timber.d("onLoadingState: $state")
        ingredientAdapter
            .updateAsync(
                createLoadingItems(state.itemCount)
            )
    }

    private fun onErrorState(state: IngredientsState.Error) {
        Timber.e(state.throwable, "Failed to get ingredients")
    }

    private fun createIngredientItems(ingredients: List<IngredientUiModel>): List<IngredientItem> {
        return ingredients.map {
            IngredientItem(
                ingredient = it,
                onLongClick = this::onIngredientLongClicked
            )
        }
    }

    private fun createLoadingItems(itemCount: Int): List<LoadingIngredientItem> {
        val items = mutableListOf<LoadingIngredientItem>()
        for (i in 0 until itemCount) {
            items.add(LoadingIngredientItem())
        }
        return items
    }
}

class IngredientItem(
    private val ingredient: IngredientUiModel,
    private val onLongClick: (IngredientUiModel) -> Unit
) : BindableItem<ListItemIngredientBinding>() {

    override fun bind(viewBinding: ListItemIngredientBinding, position: Int) {
        viewBinding.run {
            root.setOnLongClickListener {
                onLongClick(ingredient)
                true
            }
            ingredientNameTv.text = ingredient.name
            ingredientQuantityTv.run {
                text = ingredient.quantity
                visibility = ingredient.quantityVisibility()
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.list_item_ingredient
    }

    override fun initializeViewBinding(view: View): ListItemIngredientBinding {
        return ListItemIngredientBinding.bind(view)
    }
}

class LoadingIngredientItem : EmptyBindableItem<ListItemIngredientLoadingBinding>() {

    override fun getLayout(): Int {
        return R.layout.list_item_ingredient_loading
    }

    override fun initializeViewBinding(view: View): ListItemIngredientLoadingBinding {
        return ListItemIngredientLoadingBinding.bind(view)
    }
}
