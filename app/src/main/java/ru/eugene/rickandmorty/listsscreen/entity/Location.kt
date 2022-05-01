package ru.eugene.rickandmorty.listsscreen.entity

data class Location(
    override val id: Int,
    override val name: String,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)

data class LocationDetails(
    override val id: Int,
    override val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Character> = emptyList(),
    val created: String,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)