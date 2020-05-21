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
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.view_model.ConnectivityViewModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_connectivity_error.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkConnectivityErrorFragment : DialogFragment() {

    private val args: DrinkConnectivityErrorFragmentArgs by navArgs()
    private val connectivityViewModel: ConnectivityViewModel by viewModel()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.id) }

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
        lifecycleScope.launch(Dispatchers.IO) {
            if (drinkViewModel.refreshDrink()) {
                backToDrink()
            } else {
                Timber.d("onRetry failed:")
            }
        }
    }

    private fun backToDrink() {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigate(
                DrinkConnectivityErrorFragmentDirections.actionConnectivityErrorFragmentToDrinkFragment(
                    args.id
                )
            )
        }
    }
}