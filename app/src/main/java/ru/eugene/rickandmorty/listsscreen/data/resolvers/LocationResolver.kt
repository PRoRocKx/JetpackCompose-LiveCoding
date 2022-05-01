package ru.eugene.rickandmorty.listsscreen.data.resolvers

import android.content.Context
import ru.eugene.rickandmorty.listsscreen.data.*
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteLocation

class LocationResolver(context: Context) : BaseResolver<FavoriteLocation>(context) {

    override val contentUri = LOCATION_CONTENT_URI
    override val existsContentUri = EXISTS_LOCATION_CONTENT_URI

    override fun createEntity(id: Int, date: Long) = FavoriteLocation(id, date)
}