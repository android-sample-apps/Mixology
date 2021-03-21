package com.yanivsos.mixological.v2.landingPage.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentLandingPageBinding
import com.yanivsos.mixological.databinding.TileItemDrinkPreviewBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.fromLink
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.SpacerItemDecoration
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.DrinkPreviewOptionsBottomFragment
import com.yanivsos.mixological.ui.fragments.HomeFragmentDirections
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.LandingPageUiModel
import com.yanivsos.mixological.v2.landingPage.viewModel.LandingPageState
import com.yanivsos.mixological.v2.landingPage.viewModel.LandingPageViewModel
import com.yanivsos.mixological.v2.mappers.toLongId
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class LandingPageFragment : BaseFragment(R.layout.fragment_landing_page) {

    private val binding by viewBinding(FragmentLandingPageBinding::bind)
    private val landingPageViewModel: LandingPageViewModel by viewModel()
    private val latestArrivalsAdapter = GroupieAdapter()
    private val mostPopularAdapter = GroupieAdapter()
    private val recentViewedAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeLandingPage()
    }

    private fun setupAdapters() {
        val divider = SpacerItemDecoration(right = 16.dpToPx().toInt())
        initDrinkPreviewRecyclerView(binding.latestArrivalsRv, latestArrivalsAdapter, divider)
        initDrinkPreviewRecyclerView(binding.mostPopularRv, mostPopularAdapter, divider)
        initDrinkPreviewRecyclerView(binding.recentSearchesRv, recentViewedAdapter, divider)
    }

    private fun initDrinkPreviewRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        itemDecoration: RecyclerView.ItemDecoration
    ) {
        recyclerView.run {
            this.adapter = adapter
            addItemDecoration(itemDecoration)
        }
    }

    private fun observeLandingPage() {
        landingPageViewModel
            .landingPageState
            .onEach { onStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private suspend fun onStateReceived(landingPageState: LandingPageState) {
        when (landingPageState) {
            LandingPageState.Loading -> onLoadingState()
            is LandingPageState.Success -> onSuccessState(landingPageState.landingPageUiModel)
        }
    }

    private fun onLoadingState() {
        Timber.d("onLoadingState")
    }

    private suspend fun onSuccessState(landingPage: LandingPageUiModel) {
        Timber.d("onSuccessState")
        landingPage.run {
            latestArrivalsAdapter.updateAsync(latestArrivals.mapToItem())
            mostPopularAdapter.updateAsync(mostPopular.mapToItem())
            recentViewedAdapter.updateAsync(recentlyViewed.mapToItem())
            binding.recentSearchesHeader.visibility = recentlyViewed.isNotEmpty().toVisibility()
        }
    }

    private suspend fun List<DrinkPreviewUiModel>.mapToItem(): List<DrinkPreviewItem> {
        return withContext(Dispatchers.Default) {
            map { preview ->
                DrinkPreviewItem(
                    drinkPreviewUiModel = preview,
                    onDrinkClicked = this@LandingPageFragment::onDrinkPreviewClicked,
                    onDrinkLongClicked = this@LandingPageFragment::onDrinkPreviewLongClicked
                )
            }
        }
    }

    private fun onDrinkPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreviewUiModel, ScreenNames.LANDING_PAGE)
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onDrinkPreviewLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreviewUiModel, ScreenNames.LANDING_PAGE)
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
    }
}

private class DrinkPreviewItem(
    private val drinkPreviewUiModel: DrinkPreviewUiModel,
    private val onDrinkClicked: (DrinkPreviewUiModel) -> Unit,
    private val onDrinkLongClicked: (DrinkPreviewUiModel) -> Unit
) : BindableItem<TileItemDrinkPreviewBinding>() {

    override fun bind(viewBinding: TileItemDrinkPreviewBinding, position: Int) {
        viewBinding.apply {
            drinkImageIv
            drinkImageIv.fromLink(drinkPreviewUiModel.thumbnail)
            drinkNameTv.text = drinkPreviewUiModel.name
            cherryBadgeContainer
            cherryBadgeContainer.root.visibility = drinkPreviewUiModel.isFavorite.toVisibility()
            imageContainer.run {
                setOnClickListener {
                    onDrinkClicked(drinkPreviewUiModel)
                }
                setOnLongClickListener {
                    onDrinkLongClicked(drinkPreviewUiModel)
                    true
                }
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.tile_item_drink_preview
    }

    override fun initializeViewBinding(view: View): TileItemDrinkPreviewBinding {
        return TileItemDrinkPreviewBinding.bind(view)
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return when (other) {
            is DrinkPreviewItem -> other.drinkPreviewUiModel == this.drinkPreviewUiModel
            else -> super.hasSameContentAs(other)
        }
    }

    override fun getId(): Long {
        return drinkPreviewUiModel.id.toLongId()
    }
}
