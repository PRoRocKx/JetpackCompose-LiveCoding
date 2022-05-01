package ru.eugene.rickandmorty.listsscreen.extensions

const val SLASH = "/"
const val SEPARATOR = ","

fun List<String>.getIds() =
    joinToString(separator = SEPARATOR, transform = { it.substringAfterLast(SLASH) })

fun List<Int>.getIdsFromInts() = joinToString(separator = SEPARATOR)