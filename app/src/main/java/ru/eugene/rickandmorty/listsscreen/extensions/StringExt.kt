package ru.eugene.rickandmorty.listsscreen.extensions

import ru.eugene.rickandmorty.listsscreen.entity.NumberOfEpisodeInfo

private val separator by lazy { Regex("[SE]") }
private val firstZeroInSeason by lazy { Regex("^0") }

fun String.getSeasonAndEpisode(): NumberOfEpisodeInfo {
    val strings = this.split(separator, 3)
    return if (strings.size == 3 && strings[1].isDigitsOnly() && strings[2].isDigitsOnly()) {
        NumberOfEpisodeInfo(
            season = strings[1].replaceFirst(firstZeroInSeason, ""),
            episode = strings[2],
            codeOfEpisode = this
        )
    } else {
        NumberOfEpisodeInfo(codeOfEpisode = this)
    }
}

fun String.isDigitsOnly() = isNotEmpty() && all(Char::isDigit)