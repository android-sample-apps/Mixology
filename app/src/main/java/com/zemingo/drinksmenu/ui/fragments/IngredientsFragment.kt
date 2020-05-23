package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.adapters.IngredientAdapter
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.IngredientUiModel
import com.zemingo.drinksmenu.ui.models.LoadingIngredientUiModel
import com.zemingo.drinksmenu.ui.utils.InputActions
import kotlinx.android.synthetic.main.fragment_ingredients.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val LOADING_ITEM_NUMBER = 3
class IngredientsFragment : BaseDrinkFragment(R.layout.fragment_ingredients) {

    private val ingredientsAdapter = IngredientAdapter()

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
    }

    private fun initIngredientsRecyclerView() {
        ingredients_rv.run {
            adapter = ingredientsAdapter
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            }.let { addItemDecoration(it) }
        }
    }

    override fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        lifecycleScope.launch(Dispatchers.Main) {
            wrapLoadedState(drinkUiModel.ingredients)
                .collect { ingredientsAdapter.update(it) }
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

    override fun onDrinkLoading(id: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            createLoadingState()
                .flowOn(Dispatchers.IO)
                .collect { ingredientsAdapter.update(it) }
        }
    }
}