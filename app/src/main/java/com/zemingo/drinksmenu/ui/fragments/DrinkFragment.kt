package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink.*
import org.koin.android.viewmodel.ext.android.viewModel

class DrinkFragment : Fragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val drinkViewModel: DrinkViewModel by viewModel()

    init {
        lifecycleScope.launchWhenStarted { drinkViewModel.getById(args.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkViewModel
            .drink
            .observe(viewLifecycleOwner, Observer { showDrinkOnScreen(it) })
    }

    private fun showDrinkOnScreen(drinkModel: DrinkModel) {
        drink_tv.text = drinkModel.toStringUi()
    }
}