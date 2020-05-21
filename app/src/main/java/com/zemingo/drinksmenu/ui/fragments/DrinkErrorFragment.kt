package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkErrorFragment : DialogFragment() {

    private val args: DrinkErrorFragmentArgs by navArgs()
    private val connectivityViewModel: ConnectivityViewModel by viewModel()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.errorUiModel.drinkId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connectivity_error, container, false)
    }

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

        drinkViewModel
            .drink
            .observe(viewLifecycleOwner, Observer {
                onDrinkResultReceived(it)
            })
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
                DrinkErrorFragmentDirections.actionConnectivityErrorFragmentToDrinkFragment(
                    args.errorUiModel.drinkId
                )
            )
        }
    }
}