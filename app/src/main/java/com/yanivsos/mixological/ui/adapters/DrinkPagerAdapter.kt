package com.yanivsos.mixological.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yanivsos.mixological.R
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.v2.drink.fragments.IngredientsFragment
import com.yanivsos.mixological.v2.drink.fragments.MethodFragment

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
            0 -> IngredientsFragment.newInstance(drinkPreviewUiModel)
            1 -> MethodFragment.newInstance(drinkPreviewUiModel)
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
