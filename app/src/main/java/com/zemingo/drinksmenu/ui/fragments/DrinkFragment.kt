package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.ui.adapters.DrinkPagerAdapter
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_drink_collapsing.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DrinkFragment : BaseDrinkFragment(R.layout.fragment_drink_collapsing) {

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