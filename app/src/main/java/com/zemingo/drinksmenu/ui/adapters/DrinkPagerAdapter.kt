package com.zemingo.drinksmenu.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.ui.fragments.IngredientsFragment
import com.zemingo.drinksmenu.ui.fragments.MethodFragment
import com.zemingo.drinksmenu.ui.fragments.NotesFragment
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel

class DrinkPagerAdapter(
    fragment: Fragment,
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsFragment(drinkPreviewUiModel)
            1 -> MethodFragment(drinkPreviewUiModel)
            else -> throw IllegalStateException("item count is only 3")
        }
    }

    @StringRes
    fun title(position: Int): Int {
        return when (position) {
            0 -> R.string.ingredients
            1 -> R.string.method
            else -> throw IllegalStateException("item count is only 3")
        }
    }
}