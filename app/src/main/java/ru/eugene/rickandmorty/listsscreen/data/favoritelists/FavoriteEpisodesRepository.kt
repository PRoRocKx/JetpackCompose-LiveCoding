package ru.eugene.rickandmorty.listsscreen.data.favoritelists

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.EpisodeResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.data.list.Result
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.extensions.getIdsFromInts
import ru.eugene.rickandmorty.listsscreen.extensions.toEpisode

class FavoriteEpisodesRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val episodeResolver: EpisodeResolver
) : FavoriteRepository<Episode> {

    override fun getFavorites(offset: Int): Single<Result<Episode>> =
        Single.fromCallable { episodeResolver.getEntities(offset, MAX_COUNT_FAVORITES) }
            .flatMap { favoriteEpisodes ->
                favoriteEpisodes.takeIf { it.isNotEmpty() }?.let {
                    favoriteEpisodes.let(::getEpisodes)
                        .flatMap({
                            Single.fromCallable { episodeResolver.getCount() }
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

    private fun getEpisodes(favoriteEpisodes: List<FavoriteEpisode>) =
        favoriteEpisodes.takeIf { it.size > 1 }?.let {
            loadFavoriteEpisodeList(favoriteEpisodes)
        } ?: loadFavoriteEpisode(favoriteEpisodes.first())

    private fun loadFavoriteEpisodeList(favoriteEpisodes: List<FavoriteEpisode>): Single<List<Episode>> {
        val idsList = favoriteEpisodes.map { it.id }

        return rickAndMortyApi.getEpisodesByIds(idsList.getIdsFromInts())
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMap { episodeModel ->
                Observable.fromCallable { episodeResolver.exists(episodeModel.id) }
                    .map { isFavorite ->
                        episodeModel.toEpisode(isFavorite = isFavorite)
                    }
            }.toList()
            .map { episodes ->
                episodes.sortedBy { idsList.indexOf(it.id) }
            }
    }

    private fun loadFavoriteEpisode(favoriteEpisode: FavoriteEpisode) =
        Single.zip(
            rickAndMortyApi.getEpisodeById(favoriteEpisode.id),
            Single.fromCallable { episodeResolver.exists(favoriteEpisode.id) },
            { episodeModel, isFavorite ->
                listOf(episodeModel.toEpisode(isFavorite = isFavorite))
            }
        )
}