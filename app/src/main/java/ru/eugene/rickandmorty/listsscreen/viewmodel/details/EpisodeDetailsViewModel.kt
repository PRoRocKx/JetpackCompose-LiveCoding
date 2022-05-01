package ru.eugene.rickandmorty.listsscreen.viewmodel.details

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.details.EpisodeDetailsRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.data.favorite.EpisodeRoomRepository
import ru.eugene.rickandmorty.listsscreen.entity.EpisodeDetails
import java.util.*

class EpisodeDetailsViewModel : BaseDetailsViewModel<EpisodeDetails, FavoriteEpisode>(), KoinScopeComponent {

    override val repository: EpisodeDetailsRepository by inject()

    override val roomRepository: EpisodeRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteEpisode(id, Date().time))

    fun loadEpisodeDetails(id: Int) = loadDetails(id)

    fun changeEpisodeFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}