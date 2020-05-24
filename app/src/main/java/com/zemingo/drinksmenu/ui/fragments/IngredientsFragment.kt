package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.adapters.IngredientAdapter
import com.zemingo.drinksmenu.ui.models.*
import com.zemingo.drinksmenu.ui.utils.InputActions
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_ingredients.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val LOADING_ITEM_NUMBER = 3


class IngredientsFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : Fragment(R.layout.fragment_ingredients) {

    private val ingredientsAdapter = IngredientAdapter()

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
                    IngredientBottomSheetDialogFragment(it.name).show(childFragmentManager)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIngredientsRecyclerView()
        drinkViewModel
            .drink
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultUiModel.Success -> onDrinkReceived(it.data)
                    is ResultUiModel.Loading -> onDrinkLoading()
                }
            })
    }

    private fun initIngredientsRecyclerView() {
        ingredients_rv.run {
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
                    ingredientsAdapter.update(it) }
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
                    ingredientsAdapter.update(it) }
        }
    }
}