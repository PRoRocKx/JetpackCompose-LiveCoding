package ru.eugene.rickandmorty.listsscreen.data.details

import android.net.Uri
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.CharacterResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.*
import ru.eugene.rickandmorty.listsscreen.extensions.getIds
import ru.eugene.rickandmorty.listsscreen.extensions.isDigitsOnly
import ru.eugene.rickandmorty.listsscreen.extensions.toEpisode
import ru.eugene.rickandmorty.listsscreen.extensions.transformToString
import ru.eugene.rickandmorty.listsscreen.viewmodel.FIRST_PAGE

val onlyDigitsAndSeparator = Regex("^(?:\\d+,)+(?:\\d)+\$")
private const val SPACE = " "

class CharacterDetailsRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val characterResolver: CharacterResolver
) : DetailsRepository<CharacterDetails> {

    override fun getData(id: Int): Single<CharacterDetails> =
        Single.zip(
            rickAndMortyApi.getCharacterById(id),
            Single.fromCallable { characterResolver.exists(id) },
            { characterModel, isFavorite ->
                CharacterDetails(
                    id = characterModel.id,
                    name = characterModel.name,
                    status = AliveStatus.fromString(characterModel.status),
                    species = characterModel.species,
                    gender = characterModel.gender,
                    location = characterModel.locationOfCharacter,
                    image = Uri.parse(characterModel.image),
                    created = characterModel.created.transformToString(),
                    isFavorite = isFavorite
                ) to characterModel.episodeUrls
            })
            .flatMap { (characterDetails, episodeUrls) ->
                Single.zip(
                    episodeUrls.getIds().let(::getEpisodes),
                    getSimilarCharacters(characterDetails),
                    { episodeList, similarCharacters ->
                        characterDetails.copy(
                            episodes = episodeList,
                            similarCharacters = similarCharacters
                        )
                    }
                )
            }

    private fun getEpisodes(episodesIds: String): Single<List<Episode>> =
        when {
            episodesIds.isDigitsOnly() -> {
                rickAndMortyApi.getEpisodeById(episodesIds.toInt()).map { listOf(it.toEpisode()) }
            }
            episodesIds.contains(onlyDigitsAndSeparator) -> {
                rickAndMortyApi.getEpisodesByIds(episodesIds).map { list ->
                    list.map { it.toEpisode() }
                }
            }
            else -> Single.just(emptyList())
        }

    private fun getSimilarCharacters(character: CharacterDetails) =
        rickAndMortyApi.searchCharacterList(FIRST_PAGE, character.name.substringBefore(SPACE))
            .map { response ->
                response.characters.filter { it.id != character.id }
                    .map { model ->
                        SimilarCharacter(
                            id = model.id,
                            name = model.name,
                            image = Uri.parse(model.image)
                        )
                    }
            }
}