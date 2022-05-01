package ru.eugene.rickandmorty.listsscreen.db.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM FavoriteEpisode ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getEpisodes(offset: String, limit: String): Cursor

    @Query("SELECT id FROM FavoriteEpisode")
    fun getCount(): Cursor

    @Query("SELECT 1 FROM FavoriteEpisode WHERE id = :id")
    fun exists(id: String): Cursor

    @Query("REPLACE INTO FavoriteEpisode(id, date) VALUES(:id, :date)")
    fun addEpisode(id: Int, date: Long)

    @Query("DELETE FROM FavoriteEpisode WHERE id = :id")
    fun deleteEpisode(id: String): Int
}