package ru.eugene.rickandmorty.listsscreen.entity

abstract class Entity(
    open val id: Int,
    open val name: String,
    open val isFavorite: Boolean,
    open val dateFavorite: Long
)