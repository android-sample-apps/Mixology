package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.dpToPx
import com.zemingo.drinksmenu.ui.SpacerItemDecoration
import com.zemingo.drinksmenu.ui.adapters.MethodAdapter
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import com.zemingo.drinksmenu.ui.view_model.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_method.*
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class MethodFragment(
    private val drinkPreviewUiModel: DrinkPreviewUiModel
) : Fragment(R.layout.fragment_method) {

    private val methodAdapter = MethodAdapter()

    @Suppress("RemoveExplicitTypeArguments")
    private val drinkViewModel: DrinkViewModel by lazy {
        requireParentFragment().getViewModel<DrinkViewModel> { parametersOf(drinkPreviewUiModel.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMethodRecyclerView()
        drinkViewModel
            .drink
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultUiModel.Success -> onDrinkReceived(it.data)
                    is ResultUiModel.Loading -> onDrinkLoading()
                }
            })
    }

    private fun initMethodRecyclerView() {
        method_rv.run {
            adapter = methodAdapter
            addItemDecoration(
                SpacerItemDecoration(bottom = 8.dpToPx().toInt())
            )
        }
    }

    private fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        methodAdapter.update(drinkUiModel.instructions)
    }

    private fun onDrinkLoading() {
        methodAdapter.apply {
            update(listOf(SpannableString(getString(R.string.loading_method))))
        }
    }
}