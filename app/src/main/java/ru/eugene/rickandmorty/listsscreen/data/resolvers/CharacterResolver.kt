package ru.eugene.rickandmorty.listsscreen.data.resolvers

import android.content.Context
import ru.eugene.rickandmorty.listsscreen.data.*
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter

class CharacterResolver(context: Context) : BaseResolver<FavoriteCharacter>(context) {

    override val contentUri = CHARACTER_CONTENT_URI
    override val existsContentUri = EXISTS_CHARACTER_CONTENT_URI

    override fun createEntity(id: Int, date: Long) = FavoriteCharacter(id, date)
}