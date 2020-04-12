package com.zemingo.cocktailmenu.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zemingo.cocktailmenu.R
import com.zemingo.cocktailmenu.models.CocktailItemUiModel
import com.zemingo.cocktailmenu.ui.adapters.CocktailMenuAdapter
import kotlinx.android.synthetic.main.fragment_cocktail_menu.*

class CocktailMenuFragment : Fragment(R.layout.fragment_cocktail_menu) {

    private val cocktailAdapter = CocktailMenuAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCocktailRecyclerView()
        updateCocktails()
    }

    private fun updateCocktails() {
        cocktailAdapter.update(
            listOf(
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_beer
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_brandy
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_champ
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_cocktail
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_high_martini
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_highbowl
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_lowbowl
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_martini
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_pint
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_sniffter
                ),
                CocktailItemUiModel(
                    name = "Mojito",
                    ingredients = "tequila, simple syrup & mint",
                    glassIcon = R.drawable.ic_glass_wine
                )
            )
        )
    }

    private fun initCocktailRecyclerView() {
        cocktails_rv.adapter = cocktailAdapter
    }
}