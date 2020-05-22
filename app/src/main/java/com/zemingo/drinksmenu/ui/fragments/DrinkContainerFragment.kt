package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.models.DrinkErrorUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink_container.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkContainerFragment : Fragment(R.layout.fragment_drink_container) {

    private val args: DrinkFragmentArgs by navArgs()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            drinkViewModel
                .drinkFlow
                .collect { onResultReceived(it) }
        }
    }

    private fun onResultReceived(resultUiModel: ResultUiModel<DrinkUiModel>) {
        when (resultUiModel) {
            is ResultUiModel.Success -> navigateToDrink(resultUiModel.data)
            is ResultUiModel.Error -> navigateToError(resultUiModel.errorUiModel)
            is ResultUiModel.Loading -> navigateToLoading(resultUiModel.id)
        }
    }

    private fun navigateToError(errorUiModel: DrinkErrorUiModel) {
        Timber.d("navigating to error [$errorUiModel}]")
        Navigation
            .findNavController(drink_container_nav_host)
            .run {
                val navGraph = navInflater.inflate(R.navigation.drink_nav_graph)
                navGraph.startDestination = R.id.drinkErrorFragment
                setGraph(navGraph, bundleOf("errorUiModel" to errorUiModel))
            }
    }

    private fun navigateToDrink(drinkUiModel: DrinkUiModel) {
        Timber.d("navigating to drinkId[${drinkUiModel.id}]")
        Navigation
            .findNavController(drink_container_nav_host)
            .run {
                val navGraph = navInflater.inflate(R.navigation.drink_nav_graph)
                navGraph.startDestination = R.id.drinkFragment
                setGraph(navGraph, bundleOf("id" to drinkUiModel.id))
            }
    }


    private fun navigateToLoading(id: String) {
        Timber.d("navigating to loading [$id}]")
        Navigation
            .findNavController(drink_container_nav_host)
            .run {
                val navGraph = navInflater.inflate(R.navigation.drink_nav_graph)
                navGraph.startDestination = R.id.loadingDrinkFragment
                setGraph(navGraph, bundleOf("id" to id))
            }
    }
}