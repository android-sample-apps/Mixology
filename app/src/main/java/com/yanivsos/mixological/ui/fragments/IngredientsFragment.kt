package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentIngredientsBinding
import com.yanivsos.mixological.extensions.toBundle
import com.yanivsos.mixological.extensions.toDrinkPreviewUiModel
import com.yanivsos.mixological.ui.adapters.IngredientAdapter
import com.yanivsos.mixological.ui.models.*
import com.yanivsos.mixological.ui.utils.InputActions
import com.yanivsos.mixological.ui.view_model.DrinkViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val LOADING_ITEM_NUMBER = 3

class IngredientsFragment : BaseFragment(R.layout.fragment_ingredients) {

    private val binding by viewBinding(FragmentIngredientsBinding::bind)

    companion object {
        fun newInstance(drinkPreviewUiModel: DrinkPreviewUiModel): IngredientsFragment {
            return IngredientsFragment().apply {
                arguments = drinkPreviewUiModel.toBundle()
            }
        }
    }

    private val ingredientsAdapter = IngredientAdapter()

    private val drinkPreviewUiModel: DrinkPreviewUiModel by lazy {
        requireArguments().toDrinkPreviewUiModel()!!
    }

    @Suppress("RemoveExplicitTypeArguments")
    private val drinkViewModel: DrinkViewModel by lazy {
        requireParentFragment().getViewModel<DrinkViewModel> { parametersOf(drinkPreviewUiModel.id) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            ingredientsAdapter
                .inputActions.filterIsInstance<InputActions.LongClick<LoadingIngredientUiModel.Loaded>>()
                .map { it.data.ingredient }
                .collect {
                    onIngredientLongClicked(it)
                }
        }
    }

    private fun onIngredientLongClicked(ingredientUiModel: IngredientUiModel) {
        AnalyticsDispatcher.onIngredientLongClicked(ingredientUiModel, ScreenNames.INGREDIENTS)
        IngredientBottomSheetDialogFragment(ingredientUiModel).show(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIngredientsRecyclerView()
        observeDrink()
    }

    private fun observeDrink() {
        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drink
                .observe(viewLifecycleOwner, {
                    when (it) {
                        is ResultUiModel.Success -> onDrinkReceived(it.data)
                        is ResultUiModel.Loading -> onDrinkLoading()
                    }
                })
        }
    }

    private fun initIngredientsRecyclerView() {
        binding.ingredientsRv.run {
            adapter = ingredientsAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }
    }

    private fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        lifecycleScope.launch(Dispatchers.Main) {
            wrapLoadedState(drinkUiModel.ingredients)
                .flowOn(Dispatchers.IO)
                .collect {
                    Timber.d("onDrinkReceived: $it")
                    ingredientsAdapter.update(it)
                }
        }
    }

    private fun createLoadingState() = flow<List<LoadingIngredientUiModel>> {
        emit(
            mutableListOf<LoadingIngredientUiModel>().apply {
                for (i in 0 until LOADING_ITEM_NUMBER) {
                    add(LoadingIngredientUiModel.Loading)
                }
            }
        )
    }

    private fun wrapLoadedState(ingredients: List<IngredientUiModel>) =
        flow<List<LoadingIngredientUiModel>> {
            emit(
                ingredients.map { LoadingIngredientUiModel.Loaded(it) }
            )
        }

    private fun onDrinkLoading() {
        lifecycleScope.launch(Dispatchers.Main) {
            createLoadingState()
                .flowOn(Dispatchers.IO)
                .collect {
                    Timber.d("onDrinkLoading: $it")
                    ingredientsAdapter.update(it)
                }
        }
    }
}
