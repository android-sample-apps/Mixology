package com.yanivsos.mixological.v2.drink.fragments

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.FragmentMethodBinding
import com.yanivsos.mixological.databinding.ListItemMethodBinding
import com.yanivsos.mixological.databinding.ListItemMethodLoadingBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.toBundle
import com.yanivsos.mixological.extensions.toDrinkPreviewUiModel
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.v2.drink.view_model.DrinkViewModel
import com.yanivsos.mixological.v2.drink.EmptyBindableItem
import com.yanivsos.mixological.v2.drink.view_model.MethodState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class MethodFragment : BaseFragment(R.layout.fragment_method) {

    companion object {
        fun newInstance(drinkPreviewUiModel: DrinkPreviewUiModel): MethodFragment {
            return MethodFragment().apply {
                arguments = drinkPreviewUiModel.toBundle()
            }
        }
    }

    private val binding by viewBinding(FragmentMethodBinding::bind)
    private val methodAdapter = GroupieAdapter()
    private val drinkPreviewUiModel: DrinkPreviewUiModel by lazy {
        requireArguments().toDrinkPreviewUiModel()!!
    }
    private val drinkViewModel: DrinkViewModel by lazy {
        requireParentFragment().getViewModel<DrinkViewModel> { parametersOf(drinkPreviewUiModel.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMethodRecyclerView()
        observeMethod()
    }

    private fun initMethodRecyclerView() {
        binding.methodRv.run {
            adapter = methodAdapter
            addItemDecoration(
                SpacerItemDecoration(bottom = 8.dpToPx().toInt())
            )
        }
    }

    private fun observeMethod() {
        drinkViewModel
            .method
            .onEach { onMethodStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onMethodStateReceived(methodState: MethodState) {
        when (methodState) {
            is MethodState.Error -> onErrorState(methodState)
            is MethodState.Loading -> onLoadingState(methodState)
            is MethodState.Success -> onSuccessState(methodState)
        }
    }

    private fun onErrorState(methodState: MethodState.Error) {
        Timber.e(methodState.throwable, "failed to get method")
    }

    private fun onLoadingState(methodState: MethodState.Loading) {
        Timber.d("onLoadingState: $methodState")
        methodAdapter.updateAsync(
            createLoadingItem(methodState.itemCount)
        )
    }

    private fun onSuccessState(methodState: MethodState.Success) {
        Timber.d("onSuccessState: $methodState")
        methodAdapter.updateAsync(
            methodState.method.map { MethodItem(it) }
        )
    }

    private fun createLoadingItem(itemCount: Int): List<LoadingMethodItem> {
        return mutableListOf<LoadingMethodItem>().apply {
            for (i in 0 until itemCount) {
                add(LoadingMethodItem())
            }
        }
    }
}

class MethodItem(private val method: SpannableString) : BindableItem<ListItemMethodBinding>() {

    override fun bind(viewBinding: ListItemMethodBinding, position: Int) {
        viewBinding.methodTv.text = method
    }

    override fun getLayout(): Int {
        return R.layout.list_item_method
    }

    override fun initializeViewBinding(view: View): ListItemMethodBinding {
        return ListItemMethodBinding.bind(view)
    }

}

class LoadingMethodItem : EmptyBindableItem<ListItemMethodLoadingBinding>() {

    override fun getLayout(): Int {
        return R.layout.list_item_method_loading
    }

    override fun initializeViewBinding(view: View): ListItemMethodLoadingBinding {
        return ListItemMethodLoadingBinding.bind(view)
    }

}
