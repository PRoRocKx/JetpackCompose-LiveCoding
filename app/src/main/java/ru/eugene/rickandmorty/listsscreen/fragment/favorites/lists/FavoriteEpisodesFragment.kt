package ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.fragment.details.EPISODE_ID
import ru.eugene.rickandmorty.listsscreen.recycler.EpisodeListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteEpisodesViewModel

class FavoriteEpisodesFragment :
    FavoriteFragment<Episode, FavoriteEpisode, EpisodeListAdapter.EpisodeViewHolder>(R.layout.fragment_list) {

    @DimenRes
    override val startMargin = R.dimen.episode_list_divider_start_margin
    override val viewModel by viewModel<FavoriteEpisodesViewModel>()
    override val adapter by lazy {
        EpisodeListAdapter(
            R.layout.item_episode,
            ::clickItem,
            ::onFavoriteIconClicked
        )
    }

    override fun loadFirstPage(isRefresh: Boolean) = viewModel.loadFirstEpisodeList(isRefresh)

    override fun loadNextData() = viewModel.loadNextEpisodeList()

    override fun onItemWithIdClicked(id: Int) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_episodeDetailsFragment,
            bundleOf(EPISODE_ID to id)
        )
        isOpenedDetails = true
    }
}