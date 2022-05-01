package ru.eugene.rickandmorty.listsscreen.entity

import android.net.Uri
import ru.eugene.rickandmorty.listsscreen.pojo.LocationOfCharacter

data class Character(
    override var id: Int,
    override val name: String,
    val status: AliveStatus,
    val image: Uri,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)

data class CharacterDetails(
    override val id: Int,
    override val name: String,
    val status: AliveStatus,
    val species: String,
    val gender: String,
    val episodes: List<Episode> = emptyList(),
    val similarCharacters: List<SimilarCharacter> = emptyList(),
    val location: LocationOfCharacter,
    val image: Uri,
    val created: String,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)

data class SimilarCharacter(
    val id: Int,
    val name: String,
    val image: Uri,
)