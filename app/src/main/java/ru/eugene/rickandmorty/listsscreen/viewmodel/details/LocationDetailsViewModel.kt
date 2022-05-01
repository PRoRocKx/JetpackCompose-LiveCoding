package ru.eugene.rickandmorty.listsscreen.viewmodel.details

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.details.LocationDetailsRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.data.favorite.LocationRoomRepository
import ru.eugene.rickandmorty.listsscreen.entity.LocationDetails
import java.util.*

class LocationDetailsViewModel : BaseDetailsViewModel<LocationDetails, FavoriteLocation>(), KoinScopeComponent {

    override val repository: LocationDetailsRepository by inject()

    override val roomRepository: LocationRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteLocation(id, Date().time))

    fun loadLocationDetails(id: Int) = loadDetails(id)

    fun changeLocationFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}