package ru.eugene.rickandmorty.listsscreen.fragment.search

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import androidx.navigation.fragment.findNavController
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.fragment.details.LOCATION_ID
import ru.eugene.rickandmorty.listsscreen.recycler.LocationListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.LocationSearchViewModel

class LocationSearchFragment :
    SearchFragment<Location, FavoriteLocation, LocationListAdapter.LocationViewHolder>(R.layout.fragment_search_list) {

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
    override val viewModel by viewModel<LocationSearchViewModel>()

    override fun onFavoriteIconClicked(id: Int, position: Int) =
        viewModel.changeLocationFavoriteStatus(id)

    override fun searchDataByName(subject: PublishSubject<String>) =
        viewModel.searchFirstPage(subject)

    override fun loadNextData() = viewModel.searchNextPage()

    override fun onItemWithIdClicked(id: Int) {
        super.onItemWithIdClicked(id)
        findNavController().navigate(
            R.id.action_locationSearchFragment_to_locationDetailsFragment,
            bundleOf(LOCATION_ID to id)
        )
    }

    override fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    ) = viewModel.blockedFavorites.observe(lifecycleOwner, observer)
}