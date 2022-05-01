package ru.eugene.rickandmorty.listsscreen.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEpisode(
    @PrimaryKey override val id: Int,
    override val date: Long
) : FavoriteEntity(id, date)