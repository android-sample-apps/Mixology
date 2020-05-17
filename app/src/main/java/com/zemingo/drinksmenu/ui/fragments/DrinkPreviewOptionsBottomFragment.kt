package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.compatColor
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkPreviewOptionsViewModel
import kotlinx.android.synthetic.main.fragment_drink_preview_options.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class DrinkPreviewOptionsBottomFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "DrinkPreviewOptionsBottomFragment"
    }

    private val optionsViewModel: DrinkPreviewOptionsViewModel by viewModel {
        parametersOf(
            drinkPreviewUiModel.id
        )
    }

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
        observeFavoriteState()
        toggle_watchlist_tv.setOnClickListener {
            addToWatchlist()
        }
    }

    private fun observeFavoriteState() {
        optionsViewModel
            .drinkLiveData
            .observe(viewLifecycleOwner, Observer {
                Timber.d("onDrinkChanged: $it")
                onFavoriteEnabled(it.isFavorite)
            })
    }

    private fun onFavoriteEnabled(isFavorite: Boolean) {
        if (isFavorite) {
            toggle_watchlist_tv.run {
                setOnClickListener { removeFromWatchlist() }
                text = getString(R.string.remove_from_favorite)
            }
            cherry_iv.setColorFilter(
                requireContext().compatColor(android.R.color.holo_red_light),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            toggle_watchlist_tv.run {
                setOnClickListener { addToWatchlist() }
                text = getString(R.string.add_to_favorites)
            }
            cherry_iv.setColorFilter(
                requireContext().compatColor(R.color.header_text_color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun addToWatchlist() {
        Timber.d("addToWatchlist: drinkId[${drinkPreviewUiModel.name}]")
        optionsViewModel.addToWatchlist(drinkPreviewUiModel.id)
    }

    private fun removeFromWatchlist() {
        Timber.d("removeFromWatchlist: drinkId[${drinkPreviewUiModel.name}]")
        optionsViewModel.removeFromWatchlist(drinkPreviewUiModel.id)
    }
}