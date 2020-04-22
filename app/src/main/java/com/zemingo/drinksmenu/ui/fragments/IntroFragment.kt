package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : Fragment(R.layout.fragment_intro) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories_button.setOnClickListener {
            findNavController()
                .navigate(IntroFragmentDirections.actionIntroFragmentToCategoryMenuFragment())
        }
        search_previews_button.setOnClickListener {
            findNavController().navigate(
                IntroFragmentDirections.actionIntroFragmentToSearchFragment()
            )
        }
    }
}