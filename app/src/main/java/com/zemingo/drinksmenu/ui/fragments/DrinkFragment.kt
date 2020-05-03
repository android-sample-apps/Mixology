package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.clearTranslucentStatusBar
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.translucentStatusBar
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.utils.AppBarStateChangeListener
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink_expanded.*
import kotlinx.android.synthetic.main.layout_drink_label.*
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class DrinkFragment : BaseDrinkFragment(R.layout.fragment_drink_expanded) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy { DrinkPagerAdapter(this) }

    override fun getViewModel(): DrinkViewModel {
        return getViewModel { parametersOf(args.id) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        translucentStatusBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
//        clearTranslucentStatusBar()
        super.onDestroyView()
    }

    private fun translucentStatusBar() {
        requireActivity().translucentStatusBar()
    }

    private fun clearTranslucentStatusBar() {
        requireActivity().clearTranslucentStatusBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
        initInfoPagerAdapter()
    }

    private fun initAppBar() {
        /*drink_app_bar.addOnOffsetChangedListener(
            AppBarStateChangeListener { _, state ->
                onAppBarStateChanged(state)
            })*/
    }

    private fun onAppBarStateChanged(state: AppBarStateChangeListener.State) {
        if (state == AppBarStateChangeListener.State.EXPANDED) {
            translucentStatusBar()
        } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
            clearTranslucentStatusBar()
        }
    }

    private fun initInfoPagerAdapter() {
        info_vp.adapter = pagerAdapter
        TabLayoutMediator(tabs, info_vp) { tab, position ->
            tab.text = getString(pagerAdapter.title(position))
            info_vp.setCurrentItem(position, true)
        }.attach()
        info_vp.currentItem = 0
    }

    override fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        updateDrinkTitle(drinkUiModel)
        updateDrinkImage(drinkUiModel)
        updateInfoCard(drinkUiModel)
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
}