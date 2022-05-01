package ru.eugene.rickandmorty.listsscreen.db.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CharacterDao {

    @Query("SELECT * FROM FavoriteCharacter ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getCharacters(offset: String, limit: String): Cursor

    @Query("SELECT id FROM FavoriteCharacter")
    fun getCount(): Cursor

    @Query("SELECT 1 FROM FavoriteCharacter WHERE id = :id")
    fun exists(id: String): Cursor

    @Query("REPLACE INTO FavoriteCharacter(id, date) VALUES(:id, :date)")
    fun addCharacter(id: Int, date: Long)

    @Query("DELETE FROM FavoriteCharacter WHERE id = :id")
    fun deleteCharacter(id: String): Int
}