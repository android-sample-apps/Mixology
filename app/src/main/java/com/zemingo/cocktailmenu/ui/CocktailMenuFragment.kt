package com.zemingo.cocktailmenu.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.zemingo.cocktailmenu.R
import com.zemingo.cocktailmenu.models.DrinksPreviewListItemUiModel
import com.zemingo.cocktailmenu.ui.adapters.CocktailMenuAdapter
import com.zemingo.cocktailmenu.view_model.DrinksViewModel
import kotlinx.android.synthetic.main.fragment_cocktail_menu.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class CocktailMenuFragment : Fragment(R.layout.fragment_cocktail_menu) {

    private val cocktailAdapter = CocktailMenuAdapter()
    private val drinksViewModel: DrinksViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCocktailRecyclerView()
//        observeDrinks()
        observeCocktailMenu()
        lifecycleScope.launch {
            whenCreated {
                drinksViewModel.getCocktailMenu()
            }
        }
    }

    /*private fun observeDrinks() {
        drinksViewModel
            .randomCocktailLiveData
            .observe(viewLifecycleOwner, Observer { updateCocktails(it) })
    }*/

    private fun observeCocktailMenu() {
        drinksViewModel
            .cocktailMenuLiveData
            .observe(viewLifecycleOwner, Observer { updateCocktails(it) })
    }

    private fun updateCocktails(drink: DrinksPreviewListItemUiModel) {
        cocktailAdapter.update(drink.drinks)
    }

    private fun initCocktailRecyclerView() {
        cocktails_rv.adapter = cocktailAdapter
    }
}