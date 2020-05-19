package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.shareDrink
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.utils.MyTransitionListener
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.layout_drink_label.*
import kotlinx.android.synthetic.main.view_favorite_card.*
import kotlinx.android.synthetic.main.view_share_card.*
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class DrinkFragment : BaseDrinkFragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy { DrinkPagerAdapter(this) }

    override fun getViewModel(): DrinkViewModel {
        return getViewModel { parametersOf(args.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayoutListener()
        initFavoriteToggle()
        initInfoPagerAdapter()
        observeIsFavorite()
    }

    private fun initFavoriteToggle() {
        favorite_card_container.setOnClickListener {
            getViewModel().toggleFavorite()
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
                Timber.d("onTransitionChange: startId[$startId], endId[$endId], progress[$progress]")
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
        getViewModel()
            .isFavoriteLiveData
            .observe(viewLifecycleOwner, Observer { updateIsFavorite(it) })
    }

    override fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
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

    }
}