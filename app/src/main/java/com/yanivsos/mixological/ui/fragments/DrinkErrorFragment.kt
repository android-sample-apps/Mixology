package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieDrawable
import com.yanivsos.mixological.R
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.ResultUiModel
import com.yanivsos.mixological.ui.view_model.ConnectivityViewModel
import com.yanivsos.mixological.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_connectivity_error.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
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
        observeDrink()
        updateErrorViews(args.errorUiModel)
        initRetryButton()
    }

    private fun observeDrink() {
        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drinkFlow
                .filterIsInstance<ResultUiModel.Success<DrinkUiModel>>()
                .collect {
                    navigateBackToDrink(it.data)
                }
        }
    }

    private fun navigateBackToDrink(drinkUiModel: DrinkUiModel) {
        Timber.d("navigating back to drink")
        requireParentFragment()
            .findNavController()
            .navigate(
                DrinkErrorFragmentDirections
                    .actionDrinkErrorFragmentToDrinkFragment(
                        DrinkPreviewUiModel(drinkUiModel)
                    )
            )
    }

    private fun updateErrorViews(drinkErrorUiModel: DrinkErrorUiModel) {
        Timber.d("updateErrorViews: called with $drinkErrorUiModel")
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
    }

    private fun onRetry() {
        Timber.d("onRetry called:")
        drinkViewModel.refreshDrink()
    }
}