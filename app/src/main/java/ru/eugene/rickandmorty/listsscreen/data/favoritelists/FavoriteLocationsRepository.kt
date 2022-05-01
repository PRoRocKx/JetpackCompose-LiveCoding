package ru.eugene.rickandmorty.listsscreen.data.favoritelists

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.LocationResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.data.list.Result
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.extensions.getIdsFromInts
import ru.eugene.rickandmorty.listsscreen.extensions.toLocation

class FavoriteLocationsRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val locationResolver: LocationResolver
) : FavoriteRepository<Location> {

    override fun getFavorites(offset: Int): Single<Result<Location>> =
        Single.fromCallable {
            locationResolver.getEntities(offset, MAX_COUNT_FAVORITES)
        }.flatMap { favoriteLocations ->
            favoriteLocations.takeIf { it.isNotEmpty() }?.let {
                favoriteLocations.let(::getLocations)
                    .flatMap({
                        Single.fromCallable { locationResolver.getCount() }
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

    private fun getLocations(favoriteLocations: List<FavoriteLocation>) =
        favoriteLocations.takeIf { it.size > 1 }?.let {
            loadFavoriteLocationList(favoriteLocations)
        } ?: loadFavoriteLocation(favoriteLocations.first())

    private fun loadFavoriteLocationList(favoriteLocations: List<FavoriteLocation>): Single<List<Location>> {
        val idsList = favoriteLocations.map { it.id }
        return rickAndMortyApi.getLocationsByIds(idsList.getIdsFromInts())
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMap { locationModel ->
                Observable.fromCallable { locationResolver.exists(locationModel.id) }
                    .map { isFavorite ->
                        locationModel.toLocation(isFavorite = isFavorite)
                    }
            }.toList()
            .map { locations ->
                locations.sortedBy { idsList.indexOf(it.id) }
            }
    }

    private fun loadFavoriteLocation(favoriteLocation: FavoriteLocation) =
        Single.zip(
            rickAndMortyApi.getLocationById(favoriteLocation.id),
            Single.fromCallable { locationResolver.exists(favoriteLocation.id) },
            { locationModel, isFavorite ->
                listOf(locationModel.toLocation(isFavorite = isFavorite))
            }
        )
}