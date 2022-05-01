package ru.eugene.rickandmorty.listsscreen.viewmodel.favorites

import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.favorite.EpisodeRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteEpisodesRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.entity.Episode

class FavoriteEpisodesViewModel : FavoriteViewModel<Episode, FavoriteEpisode>() {

    override val repository: FavoriteEpisodesRepository by inject()

    override val roomRepository: EpisodeRoomRepository by inject()

    override fun addEntity(id: Int, date: Long) =
        roomRepository.addEntity(FavoriteEpisode(id, date))

    override fun Episode.setStatus(isFavorite: Boolean): Episode =
        this.copy(isFavorite = isFavorite)

    fun loadFirstEpisodeList(isRefresh: Boolean) = loadFirst(isRefresh)

    fun loadNextEpisodeList() = loadNext()
}