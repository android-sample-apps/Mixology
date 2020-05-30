package com.yanivsos.mixological.ui.fragments

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yanivsos.mixological.R
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.view_model.DrinkPreviewOptionsViewModel
import kotlinx.android.synthetic.main.fragment_drink_preview_options.*
import kotlinx.android.synthetic.main.view_favorite_card.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val TAG = "DrinkPreviewOptionsBottomFragment"

class DrinkPreviewOptionsBottomFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : BaseBottomSheetDialogFragment() {

    private val optionsViewModel: DrinkPreviewOptionsViewModel by viewModel {
        parametersOf(
            drinkPreviewUiModel.id
        )
    }

    private val favoriteColor: Int by lazy { requireContext().compatColor(R.color.cherry_red) }
    private val notFavoriteColor: Int by lazy { requireContext().compatColor(R.color.black) }
    private val favoriteElevation: Float by lazy { favorite_card_container.cardElevation }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink_preview_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDrinkName()
        observeFavoriteState()
        toggle_watchlist_btn.setOnClickListener {
            addToWatchlist()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initDrinkName() {
        drink_name.text = drinkPreviewUiModel.name
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
            toggle_watchlist_btn.run {
                setOnClickListener { removeFromWatchlist() }
                text = getString(R.string.remove_from_favorite)
            }
            animateToFavorite()

        } else {
            toggle_watchlist_btn.run {
                setOnClickListener { addToWatchlist() }
                text = getString(R.string.add_to_favorites)
            }
            animateToNotFavorite()
        }
    }

    private fun animateToNotFavorite() {
        animateFavorite(
            fromColor = favoriteColor,
            toColor = notFavoriteColor,
            fromElevation = favoriteElevation,
            toElevation = 0f
        )
    }

    private fun animateToFavorite() {
        animateFavorite(
            fromColor = notFavoriteColor,
            toColor = favoriteColor,
            fromElevation = 0f,
            toElevation = favoriteElevation
        )
    }

    private fun animateFavorite(
        @ColorInt fromColor: Int,
        @ColorInt toColor: Int,
        fromElevation: Float,
        toElevation: Float
    ) {
        val colorAnimation =
            ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
                addUpdateListener { animator ->
                    setCherryTint(animator.animatedValue as Int)
                }
            }

        val elevationAnimator = ValueAnimator.ofFloat(fromElevation, toElevation).apply {
            addUpdateListener { animator ->
                setFavoriteElevation(animator.animatedValue as Float)
            }
        }

        AnimatorSet().apply {
            duration = 250L
            play(colorAnimation)
                .with(elevationAnimator)
        }.start()

    }

    private fun setCherryTint(tint: Int) {
        cherry_iv.setColorFilter(
            tint,
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setFavoriteElevation(elevation: Float) {
        favorite_card_container.cardElevation = elevation
    }

    private fun addToWatchlist() {
        Timber.d("addToWatchlist: drinkId[${drinkPreviewUiModel.name}]")
        optionsViewModel.addToWatchlist(drinkPreviewUiModel)
    }

    private fun removeFromWatchlist() {
        Timber.d("removeFromWatchlist: drinkId[${drinkPreviewUiModel.name}]")
        optionsViewModel.removeFromWatchlist(drinkPreviewUiModel)
    }
}