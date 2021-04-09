package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieDrawable
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.databinding.FragmentConnectivityErrorBinding
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.view_model.ConnectivityViewModel
import com.yanivsos.mixological.v2.drink.viewModel.DrinkState
import com.yanivsos.mixological.v2.drink.viewModel.DrinkViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkErrorFragment : BaseFragment(R.layout.fragment_connectivity_error) {

    private val binding by viewBinding(FragmentConnectivityErrorBinding::bind)
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
        drinkViewModel
            .drink
            .withLifecycle()
            .onEach { onDrinkStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onDrinkStateReceived(drinkState: DrinkState) {
        Timber.d("onDrinkStateReceived: $drinkState")
        when (drinkState) {
            is DrinkState.Error -> setRetryButtonEnabled(true)
            is DrinkState.Success -> navigateBackToDrink(drinkState.model)
            DrinkState.Loading -> onLoadingState()
        }
    }

    private fun onLoadingState() {
        Timber.d("onLoadingState")
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
            binding.errorTitle.text = getString(title)
            binding.errorDescription.text = getString(description)
            binding.errorLottie.run {
                setAnimation(lottieAnimation)
                playAnimation()
                repeatCount = LottieDrawable.INFINITE
            }
        }
    }

    private fun setRetryButtonEnabled(isEnabled: Boolean) {
        binding.errorRetryBtn.isEnabled = isEnabled
    }

    private fun initRetryButton() {
        binding.errorRetryBtn.setOnClickListener {
            setRetryButtonEnabled(false)
            onRetry()
        }
    }

    private fun observeConnectivity() {
        connectivityViewModel
            .connectivityLiveData
            .observe(viewLifecycleOwner, { isConnected ->
                Timber.i("Connectivity change: isConnected[$isConnected]")
                if (isConnected) {
                    onRetry()
                }
            })
    }

    private fun onRetry() {
        Timber.d("onRetry called:")
        AnalyticsDispatcher.onDrinkErrorTryAgain(args.errorUiModel)
        drinkViewModel.refreshDrink()
    }
}
