package com.zemingo.drinksmenu.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.models.DrinksPreviewListItemUiModel
import com.zemingo.drinksmenu.ui.adapters.CocktailMenuAdapter
import kotlinx.android.synthetic.main.fragment_cocktail_menu.*

class CocktailMenuFragment : Fragment(R.layout.fragment_cocktail_menu) {

    private val cocktailAdapter = CocktailMenuAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCocktailRecyclerView()
        observeCocktailMenu()
    }

    private fun observeCocktailMenu() {
//        drinksViewModel
//            .cocktailMenuLiveData
//            .observe(viewLifecycleOwner, Observer { updateCocktails(it) })
    }

    private fun updateCocktails(drink: DrinksPreviewListItemUiModel) {
        cocktailAdapter.update(drink.drinks)
    }

    private fun initCocktailRecyclerView() {
        cocktails_rv.adapter = cocktailAdapter
    }
}