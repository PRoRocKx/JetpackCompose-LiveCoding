package ru.eugene.rickandmorty.listsscreen.pojo

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CharacterResponse(
    val info: Info,
    @SerializedName("results")
    val characters: List<CharacterModel>
)

data class CharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: Origin,
    @SerializedName("location")
    val locationOfCharacter: LocationOfCharacter,
    val image: String,
    @SerializedName("episode")
    val episodeUrls: List<String>,
    val url: String,
    val created: Date
)

data class Origin(
    val name: String,
    val url: String
)

data class LocationOfCharacter(
    val name: String,
    val url: String
)