package com.zemingo.drinksmenu.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.compatColor
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.shareDrink
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class DrinkFragment : Fragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy { DrinkPagerAdapter(this) }

    private val drinkViewModel: DrinkViewModel by viewModel { parametersOf(args.id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate called:")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayoutListener()
        initFavoriteToggle()
        initInfoPagerAdapter()
        observeDrink()
        observeIsFavorite()
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

    private fun observeIsFavorite() {
        drinkViewModel
            .isFavoriteLiveData
            .observe(viewLifecycleOwner, Observer { updateIsFavorite(it) })
    }

    private fun observeDrink() {
        lifecycleScope.launch(Dispatchers.Main) {
            drinkViewModel
                .drinkFlow
                .flowOn(Dispatchers.IO)
                .filterIsInstance<ResultUiModel.Success<DrinkUiModel>>()
                .map { it.data }
                .collect { onDrinkReceived(it) }
        }
    }

    private fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        updateDrinkTitle(drinkUiModel)
        updateDrinkImage(drinkUiModel)
        updateInfoCard(drinkUiModel)
        updateShare(drinkUiModel)
    }

    private fun updateShare(drinkUiModel: DrinkUiModel) {
        share_card_container.setOnClickListener {
            requireActivity().shareDrink(drinkUiModel)
        }
    }

    private fun updateDrinkTitle(drinkUiModel: DrinkUiModel) {
        drink_title.text = drinkUiModel.name
    }

    private fun updateDrinkImage(drinkUiModel: DrinkUiModel) {
        drink_header_image.fromLink(drinkUiModel.thumbnail)
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