package ru.eugene.rickandmorty.listsscreen.pojo

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("error")
    val message: String
)