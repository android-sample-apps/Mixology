package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.adapters.IngredientAdapter
import com.zemingo.drinksmenu.ui.models.IngredientUiModel
import kotlinx.android.synthetic.main.fragment_ingredients.*

class IngredientsFragment : BaseDrinkFragment(R.layout.fragment_ingredients) {

    private val ingredientsAdapter = IngredientAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIngredientsRecyclerView()
    }

    private fun initIngredientsRecyclerView() {
        ingredients_rv.run {
            adapter = ingredientsAdapter
        }
    }

    override fun onDrinkReceived(drinkModel: DrinkModel) {

        drinkModel.ingredients.map {
            IngredientUiModel(
                name = it.key,
                quantity = it.value,
                thumbnail = null
            )
        }.run {
            ingredientsAdapter.update(this)
        }

        /*ingredientsAdapter.update(
            listOf(
                IngredientUiModel(
                    "Gin", "2 shots",
                    "https://www.thecocktaildb.com/images/ingredients/gin.png"
                ),
                IngredientUiModel(
                    "Vodka", "1 shot",
                    "https://www.thecocktaildb.com/images/ingredients/Vodka.png"
                ),
                IngredientUiModel(
                    "Amaretto", "3 shots",
                    "https://www.thecocktaildb.com/images/ingredients/Amaretto.png"
                )
            )
        )*/
    }
}