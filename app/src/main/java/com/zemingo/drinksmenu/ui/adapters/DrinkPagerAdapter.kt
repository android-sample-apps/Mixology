package com.zemingo.drinksmenu.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.fragments.IngredientsFragment
import com.zemingo.drinksmenu.ui.fragments.MethodFragment
import com.zemingo.drinksmenu.ui.fragments.NotesFragment

class DrinkPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsFragment()
            1 -> MethodFragment()
            2 -> NotesFragment()
            else -> throw IllegalStateException("item count is only 3")
        }
    }

    @StringRes
    fun title(position: Int): Int {
        return when (position) {
            0 -> R.string.ingredients
            1 -> R.string.method
            2 -> R.string.notes
            else -> throw IllegalStateException("item count is only 3")
        }
    }
}