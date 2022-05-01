package ru.eugene.rickandmorty.listsscreen.viewmodel.search

import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.favorite.LocationRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.list.LocationListRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location
import java.util.*

class LocationSearchViewModel : SearchViewModel<Location, FavoriteLocation>() {

    override val repository: LocationListRepository by inject()

    override val roomRepository: LocationRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteLocation(id, Date().time))

    override fun Location.setStatus(isFavorite: Boolean): Location =
        this.copy(isFavorite = isFavorite)

    fun searchFirstPage(subject: PublishSubject<String>) = searchFirst(subject)

    fun searchNextPage() = searchNext()

    fun changeLocationFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}