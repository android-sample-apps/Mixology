package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.models.DrinkErrorUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoadingDrinkFragment : Fragment(R.layout.fragment_loading_drink) {

    private val args by navArgs<LoadingDrinkFragmentArgs>()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drinkFlow
                .flowOn(Dispatchers.IO)
                .collect {
                    onResultReceived(it)
                }
        }
    }

    private fun onResultReceived(resultUiModel: ResultUiModel<DrinkUiModel>) {
        when (resultUiModel) {
            is ResultUiModel.Success -> navigateToDrink(resultUiModel.data)
            is ResultUiModel.Error -> navigateToError(resultUiModel.errorUiModel)
        }
    }

    private fun navigateToError(errorUiModel: DrinkErrorUiModel) {
        findNavController().navigate(
            LoadingDrinkFragmentDirections
                .actionLoadingDrinkFragmentToDrinkErrorFragment(errorUiModel)
        )
    }

    private fun navigateToDrink(drinkUiModel: DrinkUiModel) {
        findNavController().navigate(
            LoadingDrinkFragmentDirections
                .actionLoadingDrinkFragmentToDrinkFragment2(drinkUiModel.id)
        )
    }
}