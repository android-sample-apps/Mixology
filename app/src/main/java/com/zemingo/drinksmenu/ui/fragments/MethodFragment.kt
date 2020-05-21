package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.View
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.MethodAdapter
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.android.synthetic.main.fragment_method.*

class MethodFragment : BaseDrinkFragment(R.layout.fragment_method) {

    private val methodAdapter = MethodAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMethodRecyclerView()
    }

    private fun initMethodRecyclerView() {
        method_rv.run {
            adapter = methodAdapter
            addItemDecoration(
                SpacerItemDecoration(bottom = 8.dpToPx().toInt())
            )
        }
    }

    override fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        methodAdapter.update(drinkUiModel.instructions)
    }
}