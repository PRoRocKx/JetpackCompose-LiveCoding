package ru.eugene.rickandmorty.listsscreen.data.list

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.eugene.rickandmorty.listsscreen.data.resolvers.LocationResolver
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.extensions.toLocation
import ru.eugene.rickandmorty.listsscreen.pojo.LocationResponse

class LocationListRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val locationResolver: LocationResolver
) : ListRepository<Location> {

    override fun getData(page: Int): Single<Result<Location>> =
        rickAndMortyApi.getLocationList(page).mapToLocation()

    override fun searchData(page: Int, name: String): Single<Result<Location>> =
        rickAndMortyApi.searchLocationList(page, name).mapToLocation()

    private fun Single<LocationResponse>.mapToLocation(): Single<Result<Location>> {
        var nextPage: Int? = null
        return flatMapObservable {
            nextPage = it.info.getNextPage()
            Observable.fromIterable(it.locations)
        }.flatMap { locationModel ->
            Observable.fromCallable { locationResolver.exists(locationModel.id) }
                .map { isFavorite ->
                    locationModel.toLocation(isFavorite = isFavorite)
                }
        }.toList()
            .map {
                Result(it, nextPage)
            }
    }
}