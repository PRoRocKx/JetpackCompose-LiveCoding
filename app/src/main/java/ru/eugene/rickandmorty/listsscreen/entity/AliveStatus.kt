package ru.eugene.rickandmorty.listsscreen.entity

import ru.eugene.rickandmorty.R

enum class AliveStatus(val description: String, val color: Int) {
    ALIVE("Alive", R.color.green),
    DEAD("Dead", R.color.red),
    UNKNOWN("Unknown", R.color.light_grey);

    companion object {
        fun fromString(status: String) =
            values().find { it.description == status } ?: UNKNOWN
    }
}