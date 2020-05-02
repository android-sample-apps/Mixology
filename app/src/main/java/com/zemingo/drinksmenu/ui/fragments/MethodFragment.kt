package com.zemingo.drinksmenu.ui.fragments

import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import kotlinx.android.synthetic.main.fragment_method.*

class MethodFragment : BaseDrinkFragment(R.layout.fragment_method) {

    override fun onDrinkReceived(drinkModel: DrinkModel) {
        method_tv.text = drinkModel.instructions
    }
}