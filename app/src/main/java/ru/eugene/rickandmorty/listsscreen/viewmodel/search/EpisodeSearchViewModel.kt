package ru.eugene.rickandmorty.listsscreen.viewmodel.search

import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.favorite.EpisodeRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.list.EpisodeListRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import java.util.*

class EpisodeSearchViewModel : SearchViewModel<Episode, FavoriteEpisode>() {

    override val repository: EpisodeListRepository by inject()

    override val roomRepository: EpisodeRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteEpisode(id, Date().time))

    override fun Episode.setStatus(isFavorite: Boolean): Episode =
        this.copy(isFavorite = isFavorite)

    fun searchFirstPage(subject: PublishSubject<String>) = searchFirst(subject)

    fun searchNextPage() = searchNext()

    fun changeEpisodeFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}