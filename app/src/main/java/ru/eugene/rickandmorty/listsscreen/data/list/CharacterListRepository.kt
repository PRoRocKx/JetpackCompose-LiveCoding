package ru.eugene.rickandmorty.listsscreen.data.list

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.CharacterResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.extensions.toCharacter
import ru.eugene.rickandmorty.listsscreen.pojo.CharacterResponse

class CharacterListRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val characterResolver: CharacterResolver
) : ListRepository<Character> {

    override fun getData(page: Int): Single<Result<Character>> =
        rickAndMortyApi.getCharacterList(page).mapToCharacter()

    override fun searchData(page: Int, name: String): Single<Result<Character>> =
        rickAndMortyApi.searchCharacterList(page, name).mapToCharacter()

    private fun Single<CharacterResponse>.mapToCharacter(): Single<Result<Character>> {
        var nextPage: Int? = null
        return flatMapObservable {
            nextPage = it.info.getNextPage()
            Observable.fromIterable(it.characters)
        }.flatMap { characterModel ->
            Observable.fromCallable { characterResolver.exists(characterModel.id) }
                .map { isFavorite ->
                    characterModel.toCharacter(isFavorite = isFavorite)
                }
        }.toList()
            .map {
                Result(it, nextPage)
            }
    }
}