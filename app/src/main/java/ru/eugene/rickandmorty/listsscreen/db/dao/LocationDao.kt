package ru.eugene.rickandmorty.listsscreen.db.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM FavoriteLocation ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getLocations(offset: String, limit: String): Cursor

    @Query("SELECT id FROM FavoriteLocation")
    fun getCount(): Cursor

    @Query("SELECT 1 FROM FavoriteLocation WHERE id = :id")
    fun exists(id: String): Cursor

    @Query("REPLACE INTO FavoriteLocation(id, date) VALUES(:id, :date)")
    fun addLocation(id: Int, date: Long)

    @Query("DELETE FROM FavoriteLocation WHERE id = :id")
    fun deleteLocation(id: String): Int
}