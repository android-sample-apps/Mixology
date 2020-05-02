package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import kotlinx.android.synthetic.main.fragment_drink.*


class DrinkFragment : BaseDrinkFragment(R.layout.fragment_drink) {

    private val args: DrinkFragmentArgs by navArgs()
    private val pagerAdapter: DrinkPagerAdapter by lazy { DrinkPagerAdapter(this) }

    init {
        lifecycleScope.launchWhenStarted { drinkViewModel.getById(args.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInfoPagerAdapter()
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