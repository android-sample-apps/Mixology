package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import kotlinx.android.synthetic.main.bottom_sheet_ingredient.*

class IngredientBottomSheetDialogFragment(
    private val ingredient: String
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "IngredientBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredient_name_tv.text = ingredient
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}

