package ru.eugene.rickandmorty.listsscreen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eugene.rickandmorty.listsscreen.db.dao.CharacterDao
import ru.eugene.rickandmorty.listsscreen.db.dao.EpisodeDao
import ru.eugene.rickandmorty.listsscreen.db.dao.LocationDao
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation

@Database(entities = [FavoriteCharacter::class, FavoriteLocation::class, FavoriteEpisode::class], version = 1)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun episodeDao(): EpisodeDao
}