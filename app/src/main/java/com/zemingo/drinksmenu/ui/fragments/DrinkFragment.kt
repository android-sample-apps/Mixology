package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.extensions.clearTranslucentStatusBar
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.extensions.translucentStatusBar
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import com.zemingo.drinksmenu.ui.utils.AppBarStateChangeListener
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink.*
import org.koin.android.viewmodel.ext.android.getViewModel


class DrinkFragment : BaseDrinkFragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy { DrinkPagerAdapter(this) }

    init {
        lifecycleScope.launchWhenStarted { getViewModel().getById(args.id) }
    }

    override fun getViewModel(): DrinkViewModel {
        return getViewModel<DrinkViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        translucentStatusBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        clearTranslucentStatusBar()
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
        drink_app_bar.addOnOffsetChangedListener(
            AppBarStateChangeListener { _, state ->
                onAppBarStateChanged(state)
            })
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

    override fun onDrinkReceived(drinkModel: DrinkModel) {
        drink_header_image.fromLink(drinkModel.thumbnail)
        drink_toolbar.title = drinkModel.name
    }
}