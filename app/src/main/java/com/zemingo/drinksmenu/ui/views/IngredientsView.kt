package com.zemingo.drinksmenu.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.IngredientAdapter
import com.zemingo.drinksmenu.ui.models.IngredientUiModel
import kotlinx.android.synthetic.main.view_drink_ingredients.view.*

class IngredientsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val ingredientAdapter = IngredientAdapter()

    init {
        inflate(context, R.layout.view_drink_ingredients, this)
        initView()
    }

    fun updateIngredients(ingredients: List<IngredientUiModel>) {
        ingredientAdapter.update(ingredients)
    }

    private fun initView() {
        val verticalDivider = SpacerItemDecoration(bottom = 12.dpToPx().toInt())
        ingredients_rv.run {
            adapter = ingredientAdapter
            addItemDecoration(verticalDivider)
        }
    }
}