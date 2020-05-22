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
import com.zemingo.drinksmenu.ui.utils.InputActions
import kotlinx.android.synthetic.main.fragment_ingredients.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class IngredientsFragment : BaseDrinkFragment(R.layout.fragment_ingredients) {

    private val ingredientsAdapter = IngredientAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            ingredientsAdapter
                .inputActions.filterIsInstance<InputActions.LongClick<IngredientUiModel>>()
                .map { it.data }
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

        val loadingList = mutableListOf<IngredientUiModel>()
        for (i in 0 until 3) {
            loadingList.add(IngredientUiModel(
                name = "■■■■■",
                quantity = "■■■■■■■"
            ))
        }
        ingredientsAdapter.update(loadingList)
    }

    override fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        ingredientsAdapter.update(drinkUiModel.ingredients)
    }
}