package ru.eugene.rickandmorty.listsscreen.data.favoritelists

import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.data.list.Result

interface FavoriteRepository<E : Entity> {
    fun getFavorites(offset: Int): Single<Result<E>>
}