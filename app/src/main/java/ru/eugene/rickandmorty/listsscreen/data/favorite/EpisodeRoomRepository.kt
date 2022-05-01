package ru.eugene.rickandmorty.listsscreen.data.favorite

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.EpisodeResolver
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode

class EpisodeRoomRepository(private val episodeResolver: EpisodeResolver) :
    RoomRepository<FavoriteEpisode> {

    override fun exists(id: Int): Single<Boolean> =
        Single.fromCallable { episodeResolver.exists(id) }

    override fun addEntity(favoriteEntity: FavoriteEpisode): Completable =
        Completable.fromCallable { episodeResolver.insert(favoriteEntity) }

    override fun deleteEntity(id: Int): Completable =
        Completable.fromCallable { episodeResolver.delete(id) }
}