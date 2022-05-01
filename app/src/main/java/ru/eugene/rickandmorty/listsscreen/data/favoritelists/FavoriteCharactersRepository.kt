package ru.eugene.rickandmorty.listsscreen.data.favoritelists

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.data.resolvers.CharacterResolver
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.extensions.getIdsFromInts
import ru.eugene.rickandmorty.listsscreen.data.list.Result
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.extensions.toCharacter

const val MAX_COUNT_FAVORITES = 20

class FavoriteCharactersRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val characterResolver: CharacterResolver
) : FavoriteRepository<Character> {

    override fun getFavorites(offset: Int): Single<Result<Character>> =
        Single.fromCallable { characterResolver.getEntities(offset, MAX_COUNT_FAVORITES) }
            .flatMap { favoriteCharacters ->
                favoriteCharacters.takeIf { it.isNotEmpty() }?.let {
                    favoriteCharacters.let(::getCharacters)
                        .flatMap({
                            Single.fromCallable { characterResolver.getCount() }
                        }, { list, count ->
                            Result(
                                dataList = list,
                                nextPage = (offset + MAX_COUNT_FAVORITES).takeIf {
                                    it <= count
                                }
                            )
                        })
                } ?: Single.just(Result(emptyList(), null))
            }

    private fun getCharacters(favoriteCharacters: List<FavoriteCharacter>) =
        favoriteCharacters.takeIf { it.size > 1 }?.let {
            loadFavoriteCharacterList(favoriteCharacters)
        } ?: loadFavoriteCharacter(favoriteCharacters.first())

    private fun loadFavoriteCharacterList(favoriteCharacters: List<FavoriteCharacter>): Single<List<Character>> {
        val idsList = favoriteCharacters.map { it.id }
        return rickAndMortyApi.getCharactersByIds(idsList.getIdsFromInts())
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMap { characterModel ->
                Observable.fromCallable { characterResolver.exists(characterModel.id) }
                    .map { isFavorite ->
                        characterModel.toCharacter(isFavorite = isFavorite)
                    }
            }.toList()
            .map { characters ->
                characters.sortedBy { idsList.indexOf(it.id) }
            }
    }

    private fun loadFavoriteCharacter(favoriteCharacter: FavoriteCharacter) =
        Single.zip(
            rickAndMortyApi.getCharacterById(favoriteCharacter.id),
            Single.fromCallable { characterResolver.exists(favoriteCharacter.id) },
            { characterModel, isFavorite ->
                listOf(characterModel.toCharacter(isFavorite = isFavorite))
            }
        )
}