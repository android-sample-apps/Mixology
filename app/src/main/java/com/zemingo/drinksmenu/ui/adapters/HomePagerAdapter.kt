package com.zemingo.drinksmenu.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.fragments.CategoryMenuFragment
import com.zemingo.drinksmenu.ui.fragments.LandingPageFragment
import com.zemingo.drinksmenu.ui.fragments.WatchlistFragment

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