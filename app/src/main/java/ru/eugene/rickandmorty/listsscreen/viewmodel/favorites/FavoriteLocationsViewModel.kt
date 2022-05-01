package ru.eugene.rickandmorty.listsscreen.viewmodel.favorites

import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.favorite.LocationRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteLocationsRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location

class FavoriteLocationsViewModel : FavoriteViewModel<Location, FavoriteLocation>() {

    override val repository: FavoriteLocationsRepository by inject()

    override val roomRepository: LocationRoomRepository by inject()

    override fun addEntity(id: Int, date: Long) =
        roomRepository.addEntity(FavoriteLocation(id, date))

    override fun Location.setStatus(isFavorite: Boolean): Location =
        this.copy(isFavorite = isFavorite)

    fun loadFirstLocationList(isRefresh: Boolean) = loadFirst(isRefresh)

    fun loadNextLocationList() = loadNext()
}