package com.zemingo.drinksmenu.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.fragments.CategoryMenuFragment
import com.zemingo.drinksmenu.ui.fragments.LandingPageFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LandingPageFragment()
            1 -> CategoryMenuFragment()
            else -> LandingPageFragment()
        }
    }

    @StringRes
    fun title(position: Int): Int {
        return when (position) {
            0 -> R.string.for_you
            1 -> R.string.categories
            else -> R.string.for_you
        }
    }
}