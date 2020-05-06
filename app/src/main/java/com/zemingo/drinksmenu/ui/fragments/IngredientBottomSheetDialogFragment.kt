package com.zemingo.drinksmenu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.webSearchIntent
import com.zemingo.drinksmenu.extensions.toVisibility
import com.zemingo.drinksmenu.ui.models.IngredientDetailsUiModel
import com.zemingo.drinksmenu.ui.view_model.IngredientDetailsViewModel
import kotlinx.android.synthetic.main.bottom_sheet_ingredient.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class IngredientBottomSheetDialogFragment(
    private val ingredient: String
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "IngredientBottomSheetDialogFragment"
    }

    private val detailsViewModel: IngredientDetailsViewModel by viewModel() {
        parametersOf(
            ingredient
        )
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
            startActivity(webSearchIntent(ingredient))
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun updateName() {
        ingredient_name_tv.text = ingredient
    }

    private fun updateDrink(details: IngredientDetailsUiModel) {
        ingredient_description_tv.text = details.description

        val hasDescription = details.description?.isNotEmpty() == true
        description_container.visibility = hasDescription.toVisibility()
        no_description_tv.visibility = (!hasDescription).toVisibility()

    }
}

