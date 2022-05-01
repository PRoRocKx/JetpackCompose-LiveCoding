package ru.eugene.rickandmorty.listsscreen.fragment.search

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.fragment.details.EPISODE_ID
import ru.eugene.rickandmorty.listsscreen.recycler.EpisodeListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.EpisodeSearchViewModel

class EpisodeSearchFragment :
    SearchFragment<Episode, FavoriteEpisode, EpisodeListAdapter.EpisodeViewHolder>(
        R.layout.fragment_search_list
    ) {

    @DimenRes
    override val startMargin = R.dimen.episode_list_divider_start_margin
    override val adapter by lazy {
        EpisodeListAdapter(
            R.layout.item_episode,
            ::clickItem,
            ::onFavoriteIconClicked,
            ::observeToFavoriteClickable
        )
    }
    override val viewModel by viewModel<EpisodeSearchViewModel>()

    override fun onFavoriteIconClicked(id: Int, position: Int) =
        viewModel.changeEpisodeFavoriteStatus(id)

    override fun searchDataByName(subject: PublishSubject<String>) =
        viewModel.searchFirstPage(subject)

    override fun loadNextData() = viewModel.searchNextPage()

    override fun onItemWithIdClicked(id: Int) {
        super.onItemWithIdClicked(id)
        findNavController().navigate(
            R.id.action_episodeSearchFragment_to_episodeDetailsFragment,
            bundleOf(EPISODE_ID to id)
        )
    }

    override fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    ) = viewModel.blockedFavorites.observe(lifecycleOwner, observer)
}