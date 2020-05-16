package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkPreviewOptionsViewModel
import kotlinx.android.synthetic.main.fragment_drink_preview_options.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DrinkPreviewOptionsBottomFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : BottomSheetDialogFragment() {

    private val optionsViewModel: DrinkPreviewOptionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink_preview_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drink_name.text = drinkPreviewUiModel.name
        add_to_watchlist_tv.setOnClickListener {
            addToWatchlist()
        }
    }
    private fun addToWatchlist() {
        Timber.d("addToWatchlist: drinkId[${drinkPreviewUiModel.name}]")
        optionsViewModel.addToWatchlist(drinkPreviewUiModel.id)
    }
}