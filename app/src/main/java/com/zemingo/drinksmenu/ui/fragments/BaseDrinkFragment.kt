package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zemingo.drinksmenu.domain.models.Result
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

abstract class BaseDrinkFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected abstract fun onDrinkReceived(drinkUiModel: DrinkUiModel)

    protected open fun onDrinkError(tr: Throwable) {
        Timber.d(tr, "onDrinkError: ")
    }

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
            .observe(viewLifecycleOwner, Observer { onDrinkResultReceived(it) })
    }

    private fun onDrinkResultReceived(result: Result<DrinkUiModel>) {
        when (result) {
            is Result.Success -> onDrinkReceived(result.data)
            is Result.Error -> onDrinkError(result.tr)
        }
    }
}