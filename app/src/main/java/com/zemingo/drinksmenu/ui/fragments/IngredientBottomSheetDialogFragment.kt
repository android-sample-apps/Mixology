package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.toVisibility
import com.zemingo.drinksmenu.extensions.webSearchIntent
import com.zemingo.drinksmenu.ui.models.IngredientDetailsUiModel
import com.zemingo.drinksmenu.ui.view_model.IngredientDetailsViewModel
import kotlinx.android.synthetic.main.bottom_dialog_ingredient.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val TAG = "IngredientBottomSheetDialogFragment"

class IngredientBottomSheetDialogFragment(
    private val ingredient: String
) : BottomSheetDialogFragment() {

    private val detailsViewModel: IngredientDetailsViewModel by viewModel {
        parametersOf(
            ingredient
        )
    }

    private var webSearchQuery = ingredient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateName()
        observeDetails()
        initSearchOnlineButton()
    }

    private fun observeDetails() {
        Timber.d("observing details with $detailsViewModel")
        detailsViewModel
            .ingredientDetailsLiveData
            .observe(viewLifecycleOwner, Observer {
                updateDrink(it)
            })
    }

    private fun initSearchOnlineButton() {
        search_online_btn.setOnClickListener {
            startActivity(webSearchIntent(webSearchQuery))
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun updateName() {
        ingredient_name_tv.text = ingredient
    }

    private fun updateDrink(details: IngredientDetailsUiModel) {
        updateDescription(details)
        updateAlcoholVolume(details)
        updateWebSearchQuery(details)
    }

    private fun updateAlcoholVolume(details: IngredientDetailsUiModel) {
        alcohol_volume_tv.run {
            text = getString(R.string.alcohol_volume_abv, details.alcoholVolume)
            visibility = (details.alcoholVolume != null).toVisibility()
        }
    }

    private fun updateDescription(details: IngredientDetailsUiModel) {
        val text = details.description ?: getString(R.string.no_description_found)
        ingredient_description_tv.text = text
    }

    private fun updateWebSearchQuery(details: IngredientDetailsUiModel) {
        val postfix = if (details.isAlcoholic) ", alcohol" else ""
        webSearchQuery = "$ingredient$postfix"
    }
}

