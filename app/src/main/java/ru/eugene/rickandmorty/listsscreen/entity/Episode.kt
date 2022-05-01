package ru.eugene.rickandmorty.listsscreen.entity

data class Episode(
    override val id: Int,
    override val name: String,
    val numberOfEpisodeInfo: NumberOfEpisodeInfo,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)

data class EpisodeDetails(
    override val id: Int,
    override val name: String,
    val numberOfEpisodeInfo: NumberOfEpisodeInfo,
    val residents: List<Character> = emptyList(),
    val created: String,
    override val isFavorite: Boolean = false,
    override val dateFavorite: Long = 0L
) : Entity(id, name, isFavorite, dateFavorite)

data class NumberOfEpisodeInfo(
    val season: String? = null,
    val episode: String? = null,
    val codeOfEpisode: String
)