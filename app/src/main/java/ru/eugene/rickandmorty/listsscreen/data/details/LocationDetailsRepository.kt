package ru.eugene.rickandmorty.listsscreen.data.details

import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.LocationResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.LocationDetails
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.extensions.getIds
import ru.eugene.rickandmorty.listsscreen.extensions.isDigitsOnly
import ru.eugene.rickandmorty.listsscreen.extensions.toCharacter
import ru.eugene.rickandmorty.listsscreen.extensions.transformToString

class LocationDetailsRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val locationResolver: LocationResolver
) : DetailsRepository<LocationDetails> {

    override fun getData(id: Int): Single<LocationDetails> =
        Single.zip(
            rickAndMortyApi.getLocationById(id),
            Single.fromCallable { locationResolver.exists(id) },
            { locationModel, isFavorite ->
                LocationDetails(
                    id = locationModel.id,
                    name = locationModel.name,
                    type = locationModel.type,
                    dimension = locationModel.dimension,
                    created = locationModel.created.transformToString(),
                    isFavorite = isFavorite
                ) to locationModel.characterUrls
            })
            .flatMap({ (_, characterUrls) ->
                characterUrls.getIds().takeIf(String::isNotEmpty)?.let(::getCharacters)
                    ?: Single.just(emptyList())
            }, { (locationDetails, _), characterList ->
                locationDetails.copy(residents = characterList)
            })

    private fun getCharacters(charactersIds: String): Single<List<Character>> =
        when {
            charactersIds.isDigitsOnly() -> {
                rickAndMortyApi.getCharacterById(charactersIds.toInt())
                    .map { listOf(it.toCharacter()) }
            }
            charactersIds.contains(onlyDigitsAndSeparator) -> {
                rickAndMortyApi.getCharactersByIds(charactersIds).map { list ->
                    list.map { it.toCharacter() }
                }
            }
            else -> Single.just(emptyList())
        }
}