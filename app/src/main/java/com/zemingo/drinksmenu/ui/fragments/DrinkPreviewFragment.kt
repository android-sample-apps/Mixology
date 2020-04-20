package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.adapters.DrinkPreviewAdapter
import com.zemingo.drinksmenu.view_model.DrinkPreviewByCategoryViewModel
import kotlinx.android.synthetic.main.fragment_drink_preview.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DrinkPreviewFragment : Fragment(R.layout.fragment_drink_preview) {

    private val args: DrinkPreviewFragmentArgs by navArgs()
    private val drinkPreviewViewModel: DrinkPreviewByCategoryViewModel by viewModel {
        parametersOf(
            args.category
        )
    }
    private val adapter = DrinkPreviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drink_preview_rv.adapter = adapter
        drinkPreviewViewModel
            .drinkPreviews
            .observe(viewLifecycleOwner, Observer { adapter.update(it) })
    }
}