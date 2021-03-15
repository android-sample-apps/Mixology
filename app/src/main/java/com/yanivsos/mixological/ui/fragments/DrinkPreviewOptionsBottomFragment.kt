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
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.FragmentDrinkPreviewOptionsBinding
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.view_model.DrinkPreviewOptionsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val TAG = "DrinkPreviewOptionsBottomFragment"

class DrinkPreviewOptionsBottomFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : BaseBottomSheetDialogFragment() {

    private var binding: FragmentDrinkPreviewOptionsBinding? = null
    private val optionsViewModel: DrinkPreviewOptionsViewModel by viewModel {
        parametersOf(
            drinkPreviewUiModel.id
        )
    }

    private val favoriteColor: Int by lazy { requireContext().compatColor(R.color.cherry_red) }
    private val notFavoriteColor: Int by lazy { requireContext().compatColor(R.color.non_favorite_cherry_tint) }
    private val favoriteElevation: Float by lazy { binding?.favoriteContainer?.favoriteCardContainer?.cardElevation ?: 0f  }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentDrinkPreviewOptionsBinding.inflate(
            inflater,
            container, false
        ).run {
            binding = this
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDrinkName()
        observeFavoriteState()
        binding?.toggleWatchlistBtn?.setOnClickListener {
            addToWatchlist()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initDrinkName() {
        binding?.drinkName?.text = drinkPreviewUiModel.name
    }

    private fun observeFavoriteState() {
        optionsViewModel
            .drinkLiveData
            .observe(viewLifecycleOwner, {
                Timber.d("onDrinkChanged: $it")
                onFavoriteEnabled(it.isFavorite)
            })
    }

    private fun onFavoriteEnabled(isFavorite: Boolean) {
        binding?.toggleWatchlistBtn?.run {
            if (isFavorite) {
                setOnClickListener { removeFromWatchlist() }
                text = getString(R.string.remove_from_favorite)
                animateToFavorite()

            } else {
                setOnClickListener { addToWatchlist() }
                text = getString(R.string.add_to_favorites)
                animateToNotFavorite()
            }
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
        binding?.favoriteContainer?.cherryIv?.setColorFilter(
            tint,
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setFavoriteElevation(elevation: Float) {
        binding?.favoriteContainer?.favoriteCardContainer?.cardElevation = elevation
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
