package ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.fragment.details.LOCATION_ID
import ru.eugene.rickandmorty.listsscreen.recycler.LocationListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteLocationsViewModel

class FavoriteLocationsFragment :
    FavoriteFragment<Location, FavoriteLocation, LocationListAdapter.LocationViewHolder>(R.layout.fragment_list) {

    @DimenRes
    override val startMargin = R.dimen.location_list_divider_start_margin
    override val viewModel by viewModel<FavoriteLocationsViewModel>()
    override val adapter by lazy {
        LocationListAdapter(
            R.layout.item_location,
            ::clickItem,
            ::onFavoriteIconClicked
        )
    }

    override fun loadFirstPage(isRefresh: Boolean) = viewModel.loadFirstLocationList(isRefresh)

    override fun loadNextData() = viewModel.loadNextLocationList()

    override fun onItemWithIdClicked(id: Int) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_locationDetailsFragment,
            bundleOf(LOCATION_ID to id)
        )
        isOpenedDetails = true
    }
}