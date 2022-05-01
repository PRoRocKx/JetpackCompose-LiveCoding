package ru.eugene.rickandmorty.listsscreen.extensions

import android.net.Uri
import ru.eugene.rickandmorty.listsscreen.entity.AliveStatus
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.pojo.CharacterModel
import ru.eugene.rickandmorty.listsscreen.pojo.EpisodeModel
import ru.eugene.rickandmorty.listsscreen.pojo.LocationModel

fun CharacterModel.toCharacter(isFavorite: Boolean = false): Character =
    Character(
        id = id,
        name = name,
        status = AliveStatus.fromString(status),
        image = Uri.parse(image),
        isFavorite = isFavorite
    )

fun LocationModel.toLocation(isFavorite: Boolean = false): Location =
    Location(
        id = id,
        name = name,
        isFavorite = isFavorite
    )

fun EpisodeModel.toEpisode(isFavorite: Boolean = false): Episode =
    Episode(
        id = id,
        name = name,
        numberOfEpisodeInfo = codeOfEpisode.getSeasonAndEpisode(),
        isFavorite = isFavorite
    )