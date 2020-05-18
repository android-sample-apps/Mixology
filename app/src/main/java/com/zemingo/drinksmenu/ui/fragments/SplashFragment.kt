package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import kotlinx.coroutines.delay

private const val SPLASH_DELAY = 4000L

class SplashFragment : Fragment(R.layout.fragment_splash) {

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