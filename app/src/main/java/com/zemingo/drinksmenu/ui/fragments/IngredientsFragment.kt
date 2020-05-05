package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.adapters.IngredientAdapter
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.android.synthetic.main.fragment_ingredients.*

class IngredientsFragment : BaseDrinkFragment(R.layout.fragment_ingredients) {

    private val ingredientsAdapter = IngredientAdapter().apply {
        onLongClick = {
            IngredientBottomSheetDialogFragment().show(childFragmentManager)
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
        ingredientsAdapter.update(drinkUiModel.ingredients)
    }
}