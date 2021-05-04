package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.FragmentHomeBinding
import com.yanivsos.mixological.ui.adapters.HomePagerAdapter
import com.yanivsos.mixological.ui.utils.SimpleOnTabSelectedListener
import com.yanivsos.mixological.ui.utils.setOnSingleClickListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private var itemPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomePagerAdapter()
        binding.searchAction.setOnSingleClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAdvancedSearchFragment()
            )
        }
        binding.settingsAction.setOnSingleClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            )
        }
    }

    private fun initHomePagerAdapter() {
        val homeAdapter = HomePagerAdapter(this@HomeFragment)
        binding.homeVp.run {
            isUserInputEnabled = false
            adapter = homeAdapter
            TabLayoutMediator(binding.homeTabs, this) { tab, position ->
                tab.text = getString(homeAdapter.title(position))
                setCurrentItem(position, true)
            }.attach()

            binding.homeTabs.addOnTabSelectedListener(SimpleOnTabSelectedListener(
                onSelected = { tab ->
                    itemPosition = tab.position
                }
            ))
            currentItem = itemPosition
        }
    }
}
