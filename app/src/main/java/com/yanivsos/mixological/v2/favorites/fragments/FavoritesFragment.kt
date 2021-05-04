package com.yanivsos.mixological.v2.favorites.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.databinding.FragmentWatchlistBinding
import com.yanivsos.mixological.databinding.TileItemDrinkPreviewGridBinding
import com.yanivsos.mixological.extensions.dpToPx
import com.yanivsos.mixological.extensions.fromLink
import com.yanivsos.mixological.extensions.toVisibility
import com.yanivsos.mixological.ui.GridSpacerItemDecoration
import com.yanivsos.mixological.ui.fragments.BaseFragment
import com.yanivsos.mixological.ui.fragments.DrinkPreviewOptionsBottomFragment
import com.yanivsos.mixological.ui.fragments.HomeFragmentDirections
import com.yanivsos.mixological.ui.fragments.viewLifecycleScope
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.utils.setOnSingleClickListener
import com.yanivsos.mixological.v2.favorites.viewModel.FavoritesState
import com.yanivsos.mixological.v2.favorites.viewModel.FavoritesViewModel
import com.yanivsos.mixological.v2.mappers.toLongId
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoritesFragment : BaseFragment(R.layout.fragment_watchlist) {

    private val binding by viewBinding(FragmentWatchlistBinding::bind)
    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private val previewAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWatchlistRecyclerView()
        observeFavorites()
    }

    private fun initWatchlistRecyclerView() {
        binding.watchlistRv.run {
            adapter = previewAdapter
            addItemDecoration(
                GridSpacerItemDecoration(
                    right = 4.dpToPx().toInt(),
                    left = 4.dpToPx().toInt(),
                    bottom = 4.dpToPx().toInt()
                )
            )
        }
    }

    private fun observeFavorites() {
        favoritesViewModel
            .favoritesState
            .withLifecycle()
            .onEach { onStateReceived(it) }
            .launchIn(viewLifecycleScope())
    }

    private fun onStateReceived(favoritesState: FavoritesState) {
        when (favoritesState) {
            is FavoritesState.Loading -> onLoadingState(favoritesState)
            is FavoritesState.Success -> onSuccessState(favoritesState)
        }
    }

    private fun onLoadingState(favoritesState: FavoritesState.Loading) {
        Timber.d("onLoadingState: $favoritesState")
        update(emptyList())
    }

    private fun onSuccessState(favoritesState: FavoritesState.Success) {
        Timber.d("onSuccessState: $favoritesState")
        update(favoritesState.favorites)
    }

    private fun update(previews: List<DrinkPreviewUiModel>) {
        setEmptyStateVisibility(previews)
        updateAdapter(previews)
    }

    private fun setEmptyStateVisibility(previews: List<DrinkPreviewUiModel>) {
        binding.favoriteInstructionsContainer.root.visibility =
            previews.isEmpty().toVisibility()
    }

    private fun updateAdapter(previews: List<DrinkPreviewUiModel>) {
        previewAdapter.updateAsync(
            previews
                .map {
                    GridDrinkPreviewItem(
                        drinkPreviewUiModel = it,
                        onPreviewClicked = this::onPreviewClicked,
                        onPreviewLongClicked = this::onPreviewLongClicked
                    )
                })
    }

    private fun onPreviewClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewClicked(drinkPreviewUiModel, ScreenNames.FAVORITES)
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDrinkFragment(drinkPreviewUiModel)
        )
    }

    private fun onPreviewLongClicked(drinkPreviewUiModel: DrinkPreviewUiModel) {
        AnalyticsDispatcher.onDrinkPreviewLongClicked(drinkPreviewUiModel, ScreenNames.FAVORITES)
        DrinkPreviewOptionsBottomFragment(drinkPreviewUiModel)
            .show(childFragmentManager)
    }
}

class GridDrinkPreviewItem(
    private val drinkPreviewUiModel: DrinkPreviewUiModel,
    private val onPreviewClicked: (DrinkPreviewUiModel) -> Unit,
    private val onPreviewLongClicked: (DrinkPreviewUiModel) -> Unit,
) :
    BindableItem<TileItemDrinkPreviewGridBinding>() {

    override fun getLayout(): Int {
        return R.layout.tile_item_drink_preview_grid
    }

    override fun initializeViewBinding(view: View): TileItemDrinkPreviewGridBinding {
        return TileItemDrinkPreviewGridBinding.bind(view)
    }

    override fun bind(viewBinding: TileItemDrinkPreviewGridBinding, position: Int) {
        viewBinding.run {
            drinkImageIv.fromLink(drinkPreviewUiModel.thumbnail/*, R.drawable.bar*/)
            drinkNameTv.text = drinkPreviewUiModel.name
            cherryBadgeContainer.root.visibility = drinkPreviewUiModel.isFavorite.toVisibility()
            imageContainer.setOnSingleClickListener {
                onPreviewClicked(drinkPreviewUiModel)
            }
            imageContainer.setOnLongClickListener {
                onPreviewLongClicked(drinkPreviewUiModel)
                true
            }
        }
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return when (other) {
            is GridDrinkPreviewItem -> other.drinkPreviewUiModel == this.drinkPreviewUiModel
            else -> super.hasSameContentAs(other)
        }
    }

    override fun getId(): Long {
        return drinkPreviewUiModel.id.toLongId()
    }
}
