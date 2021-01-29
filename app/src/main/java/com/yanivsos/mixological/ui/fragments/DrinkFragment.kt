package com.yanivsos.mixological.ui.fragments

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
import com.yanivsos.mixological.extensions.compatColor
import com.yanivsos.mixological.extensions.shareDrink
import com.yanivsos.mixological.extensions.toGlideBuilder
import com.yanivsos.mixological.in_app_review.RequestInAppReviewUseCase
import com.yanivsos.mixological.ui.adapters.DrinkPagerAdapter
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.ResultUiModel
import com.yanivsos.mixological.ui.utils.MyTransitionListener
import com.yanivsos.mixological.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.layout_drink_label.*
import kotlinx.android.synthetic.main.view_favorite_card.*
import kotlinx.android.synthetic.main.view_share_card.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class DrinkFragment : BaseFragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy {
        DrinkPagerAdapter(
            this,
            args.drinkPreviewUiModel
        )
    }

    private val requestInAppReviewUseCase: RequestInAppReviewUseCase by inject()

    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.drinkPreviewUiModel.id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate called:")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayoutListener()
        updateDrinkTitle(args.drinkPreviewUiModel.name)
        updateDrinkImage(args.drinkPreviewUiModel.thumbnail)
        updateIsFavorite(args.drinkPreviewUiModel.isFavorite)
        initFavoriteToggle()
        initInfoPagerAdapter()
        observeDrink()
    }

    private fun initFavoriteToggle() {
        favorite_card_container.setOnClickListener {
            onFavoriteToggled()
        }
    }

    private fun onFavoriteToggled() {
        lifecycleScope.launch(Dispatchers.IO) {
            /*val isFavorite = drinkViewModel.toggleFavorite(args.drinkPreviewUiModel)
            if (isFavorite) {
                launchInAppReview()
            }*/

            drinkViewModel.changeMeasurementPreference()
        }
    }

    private suspend fun launchInAppReview() {
        requestInAppReviewUseCase.launchReview(requireActivity())
    }

    private fun initMotionLayoutListener() {
        drink_ml.setTransitionListener(object : MyTransitionListener() {

            private val shareElevation = share_card_container.cardElevation
            private val shareStrokeWidth = share_card_container.strokeWidth
            private val favoriteElevation = share_card_container.cardElevation
            private val favoriteStrokeWidth = share_card_container.strokeWidth

            private fun updateCard(progress: Float) {
                //1.0 -> collapsed
                //0.0 -> expanded
                val cardProgress = 1f - progress
                share_card_container.run {
                    cardElevation = shareElevation * cardProgress
                    strokeWidth = (shareStrokeWidth * cardProgress).toInt()
                }
                favorite_card_container.run {
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
        info_vp.adapter = pagerAdapter
        TabLayoutMediator(tabs, info_vp) { tab, position ->
            tab.text = getString(pagerAdapter.title(position))
            info_vp.setCurrentItem(position, true)
        }.attach()
        info_vp.currentItem = 0
    }

    private fun observeDrink() {
        drinkViewModel
            .drinkFlow
            .flowOn(Dispatchers.IO)
            .onEach { onDrinkResultReceived(it) }
            .flowOn(Dispatchers.Main)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onDrinkResultReceived(resultUiModel: ResultUiModel<DrinkUiModel>) {
        when (resultUiModel) {
            is ResultUiModel.Success -> onDrinkReceived(resultUiModel.data)
            is ResultUiModel.Error -> onErrorReceived(resultUiModel.errorUiModel)
        }
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
        share_card_container.setOnClickListener {
            AnalyticsDispatcher.onDrinkShare(args.drinkPreviewUiModel)
            requireActivity().shareDrink(drinkUiModel)
        }
    }

    private fun updateDrinkTitle(drinkName: String?) {
        drink_title.text = drinkName
    }

    private fun clearLottieView() {
        header_image_placeholder.visibility = View.GONE
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
            .into(drink_header_image)
    }

    private fun updateInfoCard(drinkUiModel: DrinkUiModel) {
        alcoholic_tv.text = drinkUiModel.alcoholic
        category_tv.text = drinkUiModel.category
        glass_tv.text = drinkUiModel.glass
    }

    private fun updateIsFavorite(isFavorite: Boolean) {
        Timber.d("drink is in favorites[$isFavorite]")
        val color = if (isFavorite) R.color.cherry_red else R.color.cherry_unselected
        setFavoriteColorFilter(color)
    }

    private fun setFavoriteColorFilter(@ColorRes color: Int) {
        cherry_iv.setColorFilter(
            requireContext().compatColor(color),
            PorterDuff.Mode.SRC_IN
        )
    }
}
