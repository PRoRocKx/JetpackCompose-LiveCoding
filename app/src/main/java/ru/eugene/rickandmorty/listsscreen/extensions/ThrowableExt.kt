package ru.eugene.rickandmorty.listsscreen.extensions

import com.google.gson.Gson
import retrofit2.HttpException
import ru.eugene.rickandmorty.listsscreen.pojo.Error

fun Throwable.parseError() =
    StringBuilder().also {
        it.append(this.javaClass.simpleName)
        if (this is HttpException) {
            it.append("\n${this.code()} ")
            it.append(this.getErrorFromJson().message)
        } else {
            it.append("\n${this.message}")
        }
    }.toString()

private fun HttpException.getErrorFromJson(): Error =
    Gson().fromJson(this.response()?.errorBody()?.string(), Error::class.java)