package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yanivsos.mixological.R
import kotlinx.coroutines.delay

private const val SPLASH_DELAY = 4000L

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            waitForSplashToEnd()
        }
    }

    private suspend fun waitForSplashToEnd() {
        delay(SPLASH_DELAY)
        navigateHome()
    }

    private fun navigateHome() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        )
    }
}