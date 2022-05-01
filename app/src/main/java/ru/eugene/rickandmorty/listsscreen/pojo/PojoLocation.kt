package ru.eugene.rickandmorty.listsscreen.pojo

import com.google.gson.annotations.SerializedName
import java.util.Date

data class LocationResponse(
    val info: Info,
    @SerializedName("results")
    val locations: List<LocationModel>
)

data class LocationModel(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @SerializedName("residents")
    val characterUrls: List<String>,
    val url: String,
    val created: Date
)