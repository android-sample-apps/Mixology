package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.models.DrinkErrorUiModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
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
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.drinkPreviewUiModel.id) }

    //todo - fix this shit or make it persistent
    private var inDrink = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inDrink = false
        lifecycleScope.launch {
            drinkViewModel
                .drinkFlow
                .collect { onResultReceived(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        inDrink = false
    }

    private fun onResultReceived(resultUiModel: ResultUiModel<DrinkUiModel>) {
        when (resultUiModel) {
            is ResultUiModel.Error -> navigateToError(resultUiModel.errorUiModel)
            is ResultUiModel.Loading -> navigateToDrink(args.drinkPreviewUiModel)
            is ResultUiModel.Success -> navigateToDrink(args.drinkPreviewUiModel)
        }
    }

    private fun navigate(@IdRes startDestination: Int, bundle: Bundle) {
        Navigation
            .findNavController(drink_container_nav_host)
            .run {
                val navGraph = navInflater.inflate(R.navigation.drink_nav_graph)
                navGraph.startDestination = startDestination
                setGraph(navGraph, bundle)
            }
    }

    private fun navigateToError(errorUiModel: DrinkErrorUiModel) {
        Timber.d("navigating to error [$errorUiModel}]")
        inDrink = false
        navigate(R.id.drinkErrorFragment, bundleOf("errorUiModel" to errorUiModel))
    }

    private fun navigateToDrink(drinkPreviewUiModel: DrinkPreviewUiModel) {
        Timber.d("navigating to drinkId[${drinkPreviewUiModel}]")
        if (!inDrink) {
            navigate(
                R.id.drinkFragment,
                bundleOf(
                    "drinkPreviewUiModel" to drinkPreviewUiModel
                )
            )
        }

        inDrink = true
    }
}