package ru.eugene.rickandmorty.listsscreen.data.details

import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.EpisodeResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.entity.EpisodeDetails
import ru.eugene.rickandmorty.listsscreen.extensions.getIds
import ru.eugene.rickandmorty.listsscreen.extensions.getSeasonAndEpisode
import ru.eugene.rickandmorty.listsscreen.extensions.isDigitsOnly
import ru.eugene.rickandmorty.listsscreen.extensions.toCharacter
import ru.eugene.rickandmorty.listsscreen.extensions.transformToString

class EpisodeDetailsRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val episodeResolver: EpisodeResolver
) : DetailsRepository<EpisodeDetails> {

    override fun getData(id: Int): Single<EpisodeDetails> =
        Single.zip(
            rickAndMortyApi.getEpisodeById(id),
            Single.fromCallable { episodeResolver.exists(id) },
            { episodeModel, isFavorite ->
                EpisodeDetails(
                    id = episodeModel.id,
                    name = episodeModel.name,
                    numberOfEpisodeInfo = episodeModel.codeOfEpisode.getSeasonAndEpisode(),
                    created = episodeModel.created.transformToString(),
                    isFavorite = isFavorite
                ) to episodeModel.characterUrls
            })
            .flatMap({ (_, characterUrls) ->
                characterUrls.getIds().takeIf(String::isNotEmpty)?.let(::getCharacters)
                    ?: Single.just(emptyList())
            }, { (episodeDetails, _), characterList ->
                episodeDetails.copy(residents = characterList)
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