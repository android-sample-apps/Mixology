package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseDrinkFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

//    protected val drinkViewModel: DrinkViewModel by sharedViewModel()

    protected abstract fun onDrinkReceived(drinkModel: DrinkModel)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDrink()
    }

    protected open fun getViewModel(): DrinkViewModel {
        return requireParentFragment().getViewModel()
    }

    private fun observeDrink() {
        getViewModel()
            .drink
            .observe(viewLifecycleOwner, Observer { onDrinkReceived(it) })
    }
}