package ru.eugene.rickandmorty.listsscreen.fragment.lists

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.fragment.details.LOCATION_ID
import ru.eugene.rickandmorty.listsscreen.recycler.LocationListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.LocationListViewModel

class LocationListFragment :
    ListFragment<Location, FavoriteLocation, LocationListAdapter.LocationViewHolder>(R.layout.fragment_list) {

    @DimenRes
    override val startMargin = R.dimen.location_list_divider_start_margin
    override val adapter by lazy {
        LocationListAdapter(
            R.layout.item_location,
            ::clickItem,
            ::onFavoriteIconClicked,
            ::observeToFavoriteClickable
        )
    }
    override val viewModel by viewModel<LocationListViewModel>()

    override fun onFavoriteIconClicked(id: Int, position: Int) =
        viewModel.changeLocationFavoriteStatus(id)

    override fun loadFirstPage(isRefresh: Boolean) = viewModel.loadFirstLocationList(isRefresh)

    override fun loadNextData() = viewModel.loadNextLocationList()

    override fun navigateToSearchFragment() =
        findNavController().navigate(R.id.action_locationListFragment_to_locationSearchFragment)

    override fun onItemWithIdClicked(id: Int) {
        isOpenedDetails = true
        findNavController().navigate(
            R.id.action_locationListFragment_to_locationDetailsFragment,
            bundleOf(LOCATION_ID to id)
        )
    }

    override fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    ) = viewModel.blockedFavorites.observe(lifecycleOwner, observer)
}