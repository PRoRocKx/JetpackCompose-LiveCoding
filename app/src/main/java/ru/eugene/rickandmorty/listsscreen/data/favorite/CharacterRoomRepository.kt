package ru.eugene.rickandmorty.listsscreen.data.favorite

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.CharacterResolver
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter

class CharacterRoomRepository(private val characterResolver: CharacterResolver) :
    RoomRepository<FavoriteCharacter> {

    override fun exists(id: Int): Single<Boolean> =
        Single.fromCallable { characterResolver.exists(id) }

    override fun addEntity(favoriteEntity: FavoriteCharacter): Completable =
        Completable.fromCallable { characterResolver.insert(favoriteEntity) }

    override fun deleteEntity(id: Int): Completable =
        Completable.fromCallable { characterResolver.delete(id) }
}