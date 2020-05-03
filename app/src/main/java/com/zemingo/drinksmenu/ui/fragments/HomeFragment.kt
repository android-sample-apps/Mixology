package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.adapters.HomePagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomePagerAdapter()
    }

    private fun initHomePagerAdapter() {
        val homeAdapter = HomePagerAdapter(this@HomeFragment)
        home_vp.run {
            adapter = homeAdapter
            TabLayoutMediator(home_tabs, this) { tab, position ->
                tab.text = getString(homeAdapter.title(position))
                setCurrentItem(position, true)
            }.attach()
            currentItem = 0
        }
    }
}