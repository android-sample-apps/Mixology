package com.yanivsos.mixological.v2.drink.fragments

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.databinding.FragmentDrinkBinding
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.extensions.shareDrink
import com.yanivsos.mixological.extensions.toGlideBuilder
import com.yanivsos.mixological.in_app_review.RequestInAppReviewUseCase
import com.yanivsos.mixological.ui.adapters.DrinkPagerAdapter
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.utils.MyTransitionListener
import com.yanivsos.mixological.v2.drink.viewModel.DrinkState
import com.yanivsos.mixological.v2.drink.viewModel.DrinkViewModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class DrinkFragment : BaseFragment(R.layout.fragment_drink) {

    private val binding by viewBinding(FragmentDrinkBinding::bind)
    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy {
        DrinkPagerAdapter(
            this,
            args.drinkPreviewUiModel
        )
    }

    private val requestInAppReviewUseCase: RequestInAppReviewUseCase by inject()
    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.drinkPreviewUiModel.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayoutListener()
        initFavoriteToggle()
        initInfoPagerAdapter()
        updateDrinkTitle(args.drinkPreviewUiModel.name)
        updateDrinkImage(args.drinkPreviewUiModel.thumbnail)
        updateIsFavorite(args.drinkPreviewUiModel.isFavorite)
        observeDrinkState()
    }

    private fun initFavoriteToggle() {
        binding.favoriteContainer.favoriteCardContainer.setOnClickListener {
            onFavoriteToggled()
        }
    }

    private fun onFavoriteToggled() {
        lifecycleScope.launch {
            val isFavorite = drinkViewModel.toggleFavorite(args.drinkPreviewUiModel)
            Timber.d("toggled favorite: isFavorite[$isFavorite]")
            if (isFavorite) {
                launchInAppReview()
            }
        }
    }

    private suspend fun launchInAppReview() {
        requestInAppReviewUseCase.launchReview(requireActivity())
    }

    private fun initMotionLayoutListener() {
        binding.drinkMl.setTransitionListener(object : MyTransitionListener() {
            val shareCardContainer = binding.shareContainer.shareCardContainer
            val favoriteContainer = binding.favoriteContainer.favoriteCardContainer

            private val shareElevation = shareCardContainer.cardElevation
            private val shareStrokeWidth = shareCardContainer.strokeWidth
            private val favoriteElevation = shareCardContainer.cardElevation
            private val favoriteStrokeWidth = shareCardContainer.strokeWidth

            private fun updateCard(progress: Float) {
                //1.0 -> collapsed
                //0.0 -> expanded
                val cardProgress = 1f - progress
                shareCardContainer.run {
                    cardElevation = shareElevation * cardProgress
                    strokeWidth = (shareStrokeWidth * cardProgress).toInt()
                }
                favoriteContainer.run {
                    cardElevation = favoriteElevation * cardProgress
                    strokeWidth = (favoriteStrokeWidth * cardProgress).toInt()
                }
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                updateCard(progress)
            }
        })
    }

    private fun initInfoPagerAdapter() {
        binding.infoVp.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.infoVp) { tab, position ->
            tab.text = getString(pagerAdapter.title(position))
            binding.infoVp.setCurrentItem(position, true)
        }.attach()
        binding.infoVp.currentItem = 0
    }

    private fun observeDrinkState() {
        drinkViewModel
            .drink
            .onEach { onDrinkStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onDrinkStateReceived(drinkState: DrinkState) {
        when (drinkState) {
            is DrinkState.Error -> onErrorState(drinkState)
            DrinkState.Loading -> onLoadingState(drinkState)
            is DrinkState.Success -> onSuccessState(drinkState)
        }
    }

    private fun onErrorState(drinkState: DrinkState.Error) {
        Timber.e(drinkState.throwable, "failed to get drink[${drinkState.drinkId}]")
        onErrorReceived(drinkState.toUiModel())
    }

    private fun onLoadingState(drinkState: DrinkState) {
        Timber.d("onLoadingState: $drinkState")
    }

    private fun onSuccessState(drinkState: DrinkState.Success) {
        Timber.d("onSuccessState: $drinkState")
        onDrinkReceived(drinkState.model)
    }

    private fun onErrorReceived(errorUiModel: DrinkErrorUiModel) {
        requireParentFragment().findNavController().navigate(
            DrinkFragmentDirections
                .actionDrinkFragmentToDrinkErrorFragment(errorUiModel)
        )
    }

    private fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        updateDrinkTitle(drinkUiModel.name)
        updateDrinkImage(drinkUiModel.thumbnail)
        updateInfoCard(drinkUiModel)
        updateShare(drinkUiModel)
        updateIsFavorite(drinkUiModel.isFavorite)
    }

    private fun updateShare(drinkUiModel: DrinkUiModel) {
        binding.shareContainer.shareCardContainer.setOnClickListener {
            AnalyticsDispatcher.onDrinkShare(args.drinkPreviewUiModel)
            requireActivity().shareDrink(drinkUiModel)
        }
    }

    private fun updateDrinkTitle(drinkName: String?) {
        binding.drinkTitle.text = drinkName
    }

    private fun clearLottieView() {
        binding.headerImagePlaceholder.visibility = View.GONE
    }

    private fun updateDrinkImage(thumbnail: String?) {
        val clearLottieLiveData = MutableLiveData<Unit>()
        clearLottieLiveData.observe(viewLifecycleOwner, {
            clearLottieView()
        })
        requireContext()
            .toGlideBuilder(thumbnail)
            .addListener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    clearLottieLiveData.postValue(Unit)
                    return false
                }
            })
            .into(binding.drinkHeaderImage)
    }

    private fun updateInfoCard(drinkUiModel: DrinkUiModel) {
        binding.infoContainer.alcoholicTv.text = drinkUiModel.alcoholic
        binding.infoContainer.categoryTv.text = drinkUiModel.category
        binding.infoContainer.glassTv.text = drinkUiModel.glass
    }

    private fun updateIsFavorite(isFavorite: Boolean) {
        Timber.d("drink is in favorites[$isFavorite]")
        val color = if (isFavorite) R.color.cherry_red else R.color.cherry_unselected
        setFavoriteColorFilter(color)
    }

    private fun setFavoriteColorFilter(@ColorRes color: Int) {
        binding.favoriteContainer.cherryIv.setColorFilter(
            requireContext().compatColor(color),
            PorterDuff.Mode.SRC_IN
        )
    }
}
