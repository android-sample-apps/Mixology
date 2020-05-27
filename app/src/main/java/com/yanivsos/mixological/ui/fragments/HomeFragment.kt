package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.yanivsos.mixological.R
import com.yanivsos.mixological.ui.adapters.HomePagerAdapter
import com.yanivsos.mixological.ui.utils.SimpleOnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var itemPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomePagerAdapter()
        search_action.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAdvancedSearchFragment()
            )
        }
    }

    private fun initHomePagerAdapter() {
        val homeAdapter = HomePagerAdapter(this@HomeFragment)
        home_vp.run {
            isUserInputEnabled = false
            adapter = homeAdapter
            TabLayoutMediator(home_tabs, this) { tab, position ->
                tab.text = getString(homeAdapter.title(position))
                setCurrentItem(position, true)
            }.attach()

            home_tabs.addOnTabSelectedListener(SimpleOnTabSelectedListener(
                onSelected = { tab ->
                    itemPosition = tab.position
                }
            ))
            currentItem = itemPosition
        }
    }
}