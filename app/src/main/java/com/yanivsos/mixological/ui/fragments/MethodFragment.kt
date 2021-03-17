/*
package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.FragmentMethodBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.toBundle
import com.yanivsos.mixological.extensions.toDrinkPreviewUiModel
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.adapters.MethodAdapter
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.LoadingMethodUiModel
import com.yanivsos.mixological.ui.models.ResultUiModel
import com.yanivsos.mixological.ui.view_model.DrinkViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class MethodFragment : BaseFragment(R.layout.fragment_method) {

    private val binding by viewBinding(FragmentMethodBinding::bind)
    companion object {
        fun newInstance(drinkPreviewUiModel: DrinkPreviewUiModel): MethodFragment {
            return MethodFragment().apply {
                arguments = drinkPreviewUiModel.toBundle()
            }
        }
    }

    private val drinkPreviewUiModel: DrinkPreviewUiModel by lazy {
        requireArguments().toDrinkPreviewUiModel()!!
    }

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
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ResultUiModel.Success -> onDrinkReceived(it.data)
                    is ResultUiModel.Loading -> onDrinkLoading()
                }
            })
    }

    private fun initMethodRecyclerView() {
        binding.methodRv.run {
            adapter = methodAdapter
            addItemDecoration(
                SpacerItemDecoration(bottom = 8.dpToPx().toInt())
            )
        }
    }

    private fun wrapLoaded(drinkUiModel: DrinkUiModel) =
        flow<List<LoadingMethodUiModel>> {
            emit(
                drinkUiModel
                    .instructions
                    .map { LoadingMethodUiModel.Loaded(it) }
            )
        }

    private fun wrapLoading() =
        flow<List<LoadingMethodUiModel>> {
            emit(
                listOf(
                    LoadingMethodUiModel.Loading
                )
            )
        }

    private fun onDrinkReceived(drinkUiModel: DrinkUiModel) {
        lifecycleScope.launch(Dispatchers.Main) {
            wrapLoaded(drinkUiModel)
                .flowOn(Dispatchers.IO)
                .collect {
                    methodAdapter.update(it)
                }
        }
    }

    private fun onDrinkLoading() {
        lifecycleScope.launch(Dispatchers.Main) {
            wrapLoading()
                .flowOn(Dispatchers.IO)
                .collect { methodAdapter.update(it) }
        }
    }
}
*/
