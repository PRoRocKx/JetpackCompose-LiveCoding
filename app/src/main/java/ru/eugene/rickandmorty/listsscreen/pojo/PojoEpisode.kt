package ru.eugene.rickandmorty.listsscreen.pojo

import com.google.gson.annotations.SerializedName
import java.util.Date

data class EpisodeResponse(
    val info: Info,
    @SerializedName("results")
    val episodes: List<EpisodeModel>
)

data class EpisodeModel(
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode")
    val codeOfEpisode: String,
    @SerializedName("characters")
    val characterUrls: List<String>,
    val url: String,
    val created: Date
)