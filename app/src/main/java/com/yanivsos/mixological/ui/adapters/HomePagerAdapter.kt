package com.yanivsos.mixological.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yanivsos.mixological.R
import com.yanivsos.mixological.ui.fragments.CategoryMenuFragment
import com.yanivsos.mixological.ui.fragments.LandingPageFragment
import com.yanivsos.mixological.ui.fragments.WatchlistFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LandingPageFragment()
            1 -> CategoryMenuFragment()
            else -> WatchlistFragment()
        }
    }

    @StringRes
    fun title(position: Int): Int {
        return when (position) {
            0 -> R.string.for_you
            1 -> R.string.categories
            else -> R.string.favorites
        }
    }
}