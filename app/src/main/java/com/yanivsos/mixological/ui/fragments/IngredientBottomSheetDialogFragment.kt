package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.databinding.BottomDialogIngredientBinding
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.extensions.webSearchIntent
import com.yanivsos.mixological.ui.models.IngredientDetailsUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import com.yanivsos.mixological.ui.view_model.IngredientDetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

private const val TAG = "IngredientBottomSheetDialogFragment"

class IngredientBottomSheetDialogFragment(
    private val ingredient: IngredientUiModel
) : BaseBottomSheetDialogFragment() {

    private var binding: BottomDialogIngredientBinding? = null

    private val detailsViewModel: IngredientDetailsViewModel by viewModel {
        parametersOf(
            ingredient.name
        )
    }

    private var webSearchQuery = ingredient.name

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        BottomDialogIngredientBinding.inflate(
            inflater,
            container,
            false
        ).run {
            binding = this
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateName()
        observeDetails()
        initSearchOnlineButton()
    }

    private fun observeDetails() {
        Timber.d("observing details with $ingredient")
        detailsViewModel
            .ingredientDetailsLiveData
            .observe(viewLifecycleOwner, {
                updateDrink(it)
            })
    }

    private fun initSearchOnlineButton() {
        binding?.searchOnlineBtn?.setOnClickListener {
            AnalyticsDispatcher.onIngredientSearchedOnline(ingredient)
            try {
                startActivity(webSearchIntent(webSearchQuery))
            } catch (e: Exception) {
                Timber.e(e, "unable to perform web search")
                dismiss()
                Toast.makeText(
                    requireContext(),
                    R.string.no_search_activity_found,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun updateName() {
        binding?.ingredientNameTv?.text = ingredient.name
    }

    private fun updateDrink(details: IngredientDetailsUiModel) {
        updateDescription(details)
        updateAlcoholVolume(details)
        updateWebSearchQuery(details)
    }

    private fun updateAlcoholVolume(details: IngredientDetailsUiModel) {
        binding?.alcoholVolumeTv?.run {
            text = getString(R.string.alcohol_volume_abv, details.alcoholVolume)
            visibility = (details.alcoholVolume != null).toVisibility()
        }
    }

    private fun updateDescription(details: IngredientDetailsUiModel) {
        val text = details.description ?: getString(R.string.no_description_found)
        binding?.ingredientDescriptionTv?.text = text
    }

    private fun updateWebSearchQuery(details: IngredientDetailsUiModel) {
        val postfix = if (details.isAlcoholic) ", alcohol" else ""
        webSearchQuery = "${ingredient.name}$postfix"
    }
}

