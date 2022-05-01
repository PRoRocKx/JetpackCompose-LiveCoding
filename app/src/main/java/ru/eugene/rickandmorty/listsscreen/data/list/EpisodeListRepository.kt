package ru.eugene.rickandmorty.listsscreen.data.list

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.EpisodeResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.extensions.toEpisode
import ru.eugene.rickandmorty.listsscreen.pojo.EpisodeResponse

class EpisodeListRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val episodeResolver: EpisodeResolver
) : ListRepository<Episode> {

    override fun getData(page: Int): Single<Result<Episode>> =
        rickAndMortyApi.getEpisodeList(page).mapToEpisode()

    override fun searchData(page: Int, name: String): Single<Result<Episode>> =
        rickAndMortyApi.searchEpisodeList(page, name).mapToEpisode()

    private fun Single<EpisodeResponse>.mapToEpisode(): Single<Result<Episode>> {
        var nextPage: Int? = null
        return flatMapObservable {
            nextPage = it.info.getNextPage()
            Observable.fromIterable(it.episodes)
        }.flatMap { episodeModel ->
            Observable.fromCallable { episodeResolver.exists(episodeModel.id) }
                .map { isFavorite ->
                    episodeModel.toEpisode(isFavorite = isFavorite)
                }
        }.toList()
            .map {
                Result(it, nextPage)
            }
    }
}