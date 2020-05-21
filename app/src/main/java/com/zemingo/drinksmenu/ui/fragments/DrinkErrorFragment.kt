package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieDrawable
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.models.DrinkErrorUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import com.zemingo.drinksmenu.ui.view_model.ConnectivityViewModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_connectivity_error.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkErrorFragment : Fragment(R.layout.fragment_connectivity_error) {

    private val args: DrinkErrorFragmentArgs by navArgs()
    private val connectivityViewModel: ConnectivityViewModel by viewModel()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.errorUiModel.drinkId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnectivity()
        updateErrorViews(args.errorUiModel)
        initRetryButton()
    }

    private fun updateErrorViews(drinkErrorUiModel: DrinkErrorUiModel) {
        drinkErrorUiModel.run {
            error_title.text = getString(title)
            error_description.text = getString(description)
            error_lottie.run {
                setAnimation(lottieAnimation)
                playAnimation()
                repeatCount = LottieDrawable.INFINITE
            }
        }
    }

    private fun initRetryButton() {
        error_retry_btn.setOnClickListener { onRetry() }
    }

    private fun observeConnectivity() {
        connectivityViewModel
            .connectivityLiveData
            .observe(viewLifecycleOwner, Observer { isConnected ->
                Timber.i("Connectivity change: isConnected[$isConnected]")
                if (isConnected) {
                    onRetry()
                }
            })

        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drinkFlow
                .flowOn(Dispatchers.IO)
                .collect { onDrinkResultReceived(it) }
        }
    }

    private fun onDrinkResultReceived(resultUiModel: ResultUiModel<DrinkUiModel>) {
        when (resultUiModel) {
            is ResultUiModel.Success -> backToDrink()
            is ResultUiModel.Error -> updateErrorViews(resultUiModel.errorUiModel)
        }
    }

    private fun onRetry() {
        Timber.d("onRetry called:")
        drinkViewModel.refreshDrink()
    }

    private fun backToDrink() {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigate(
                DrinkErrorFragmentDirections.actionDrinkErrorFragmentToDrinkFragment2(
                    args.errorUiModel.drinkId
                )
            )
        }
    }
}