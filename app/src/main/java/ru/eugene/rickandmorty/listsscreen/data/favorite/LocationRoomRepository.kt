package ru.eugene.rickandmorty.listsscreen.data.favorite

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.LocationResolver
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation

class LocationRoomRepository(private val locationResolver: LocationResolver) :
    RoomRepository<FavoriteLocation> {

    override fun exists(id: Int): Single<Boolean> =
        Single.fromCallable { locationResolver.exists(id) }

    override fun addEntity(favoriteEntity: FavoriteLocation): Completable =
        Completable.fromCallable { locationResolver.insert(favoriteEntity) }

    override fun deleteEntity(id: Int): Completable =
        Completable.fromCallable { locationResolver.delete(id) }
}