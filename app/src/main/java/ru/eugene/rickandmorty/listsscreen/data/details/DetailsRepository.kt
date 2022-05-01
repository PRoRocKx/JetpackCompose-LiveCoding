package ru.eugene.rickandmorty.listsscreen.data.details

import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.entity.Entity

interface DetailsRepository<E : Entity> {
    fun getData(id: Int): Single<E>
}