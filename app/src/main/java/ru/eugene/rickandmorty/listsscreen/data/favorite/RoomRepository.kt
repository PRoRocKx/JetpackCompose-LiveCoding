package ru.eugene.rickandmorty.listsscreen.data.favorite

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity

interface RoomRepository<FE : FavoriteEntity> {

    fun exists(id: Int): Single<Boolean>

    fun addEntity(favoriteEntity: FE): Completable

    fun deleteEntity(id: Int): Completable
}