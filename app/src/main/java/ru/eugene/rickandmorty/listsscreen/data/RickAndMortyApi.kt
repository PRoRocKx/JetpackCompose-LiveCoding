package ru.eugene.rickandmorty.listsscreen.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.eugene.rickandmorty.listsscreen.pojo.CharacterModel
import ru.eugene.rickandmorty.listsscreen.pojo.CharacterResponse
import ru.eugene.rickandmorty.listsscreen.pojo.EpisodeModel
import ru.eugene.rickandmorty.listsscreen.pojo.EpisodeResponse
import ru.eugene.rickandmorty.listsscreen.pojo.LocationModel
import ru.eugene.rickandmorty.listsscreen.pojo.LocationResponse

interface RickAndMortyApi {
    @GET("character")
    fun getCharacterList(@Query("page") page: Int): Single<CharacterResponse>

    @GET("character")
    fun searchCharacterList(
        @Query("page") page: Int,
        @Query("name") name: String
    ): Single<CharacterResponse>

    @GET("character/{id}")
    fun getCharacterById(
        @Path("id") id: Int
    ): Single<CharacterModel>

    @GET("character/{ids}")
    fun getCharactersByIds(
        @Path("ids") ids: String
    ): Single<List<CharacterModel>>


    @GET("location")
    fun getLocationList(@Query("page") page: Int): Single<LocationResponse>

    @GET("location")
    fun searchLocationList(
        @Query("page") page: Int,
        @Query("name") name: String
    ): Single<LocationResponse>

    @GET("location/{id}")
    fun getLocationById(
        @Path("id") id: Int
    ): Single<LocationModel>

    @GET("location/{ids}")
    fun getLocationsByIds(
        @Path("ids") ids: String
    ): Single<List<LocationModel>>


    @GET("episode")
    fun getEpisodeList(@Query("page") page: Int): Single<EpisodeResponse>

    @GET("episode")
    fun searchEpisodeList(
        @Query("page") page: Int,
        @Query("name") name: String
    ): Single<EpisodeResponse>

    @GET("episode/{id}")
    fun getEpisodeById(
        @Path("id") id: Int
    ): Single<EpisodeModel>

    @GET("episode/{ids}")
    fun getEpisodesByIds(
        @Path("ids") ids: String
    ): Single<List<EpisodeModel>>
}