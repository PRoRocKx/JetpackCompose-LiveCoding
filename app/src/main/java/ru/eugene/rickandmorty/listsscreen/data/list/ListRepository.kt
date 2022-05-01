package ru.eugene.rickandmorty.listsscreen.data.list

import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.entity.Entity

interface ListRepository<E : Entity> {
    fun getData(page: Int): Single<Result<E>>
    fun searchData(page: Int, name: String): Single<Result<E>>
}