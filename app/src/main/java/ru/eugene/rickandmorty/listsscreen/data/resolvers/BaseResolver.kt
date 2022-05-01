package ru.eugene.rickandmorty.listsscreen.data.resolvers

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import ru.eugene.rickandmorty.listsscreen.data.FAVORITE_DATE
import ru.eugene.rickandmorty.listsscreen.data.FAVORITE_ID
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity

abstract class BaseResolver<FE: FavoriteEntity>(val context: Context) {

    abstract val contentUri: Uri
    abstract val existsContentUri: Uri

    abstract fun createEntity(id: Int, date: Long): FE

    fun getEntities(offset: Int, limit: Int): List<FE> {
        val favorites = mutableListOf<FE>()
        context.contentResolver.query(
            contentUri,
            null,
            null,
            arrayOf(offset.toString(), limit.toString()),
            null,
            null
        )?.use {
            while (it.moveToNext()) {
                favorites.add(createEntity(it.getInt(0), it.getLong(1)))
            }
        }
        return favorites
    }

    fun insert(favoriteEntity: FE) =
        context.contentResolver.insert(
            ContentUris.withAppendedId(contentUri, favoriteEntity.id.toLong()),
            ContentValues().also {
                it.put(FAVORITE_ID, favoriteEntity.id)
                it.put(FAVORITE_DATE, favoriteEntity.date)
            }
        )

    fun delete(id: Int) =
        context.contentResolver.delete(
            ContentUris.withAppendedId(contentUri, id.toLong()),
            null,
            null
        )

    fun getCount(): Int {
        context.contentResolver.query(
            contentUri,
            null,
            null,
            null,
            null,
            null
        )?.use {
            return it.count
        }
        return 0
    }

    fun exists(id: Int): Boolean {
        context.contentResolver.query(
            ContentUris.withAppendedId(existsContentUri, id.toLong()),
            null,
            null,
            null,
            null,
            null
        )?.use {
            return it.count > 0
        }
        return false
    }
}