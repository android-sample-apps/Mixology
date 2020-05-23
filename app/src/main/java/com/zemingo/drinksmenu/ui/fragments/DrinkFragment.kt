package com.zemingo.drinksmenu.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.request.LoadRequest
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.compatColor
import com.zemingo.drinksmenu.extensions.shareDrink
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import com.zemingo.drinksmenu.ui.models.DrinkErrorUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import com.zemingo.drinksmenu.ui.utils.MyTransitionListener
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.layout_drink_label.*
import kotlinx.android.synthetic.main.view_favorite_card.*
import kotlinx.android.synthetic.main.view_share_card.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class DrinkFragment : Fragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy {
        DrinkPagerAdapter(
            this,
            args.drinkPreviewUiModel
        )
    }

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
            drinkViewModel.toggleFavorite()
        }
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
        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drinkFlow
                .flowOn(Dispatchers.IO)
                .collect { onDrinkResultReceived(it) }
        }
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
            requireActivity().shareDrink(drinkUiModel)
        }
    }

    private fun updateDrinkTitle(drinkName: String?) {
        drink_title.text = drinkName
    }

    private fun updateDrinkImage(thumbnail: String?) {
        LoadRequest.Builder(requireContext())
            .data(thumbnail)
            .crossfade(250)
            .target { drawable ->
                header_image_placeholder.visibility = View.GONE
                drink_header_image.setImageDrawable(drawable)
            }
            .build()
            .run { ImageLoader(requireContext()).execute(this) }
    }

    private fun updateInfoCard(drinkUiModel: DrinkUiModel) {
        alcoholic_tv.text = drinkUiModel.alcoholic
        category_tv.text = drinkUiModel.category
        glass_tv.text = drinkUiModel.glass
    }

    private fun updateIsFavorite(isFavorite: Boolean) {
        Timber.d("drink is in favorites[$isFavorite]")
        val color = if (isFavorite) R.color.cherry_red else R.color.black
        setFavoriteColorFilter(color)
    }

    private fun setFavoriteColorFilter(@ColorRes color: Int) {
        cherry_iv.setColorFilter(
            requireContext().compatColor(color),
            PorterDuff.Mode.SRC_IN
        )
    }
}