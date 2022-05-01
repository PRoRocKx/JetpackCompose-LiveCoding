package ru.eugene.rickandmorty.listsscreen.data.resolvers

import android.content.Context
import ru.eugene.rickandmorty.listsscreen.data.*
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEpisode

class EpisodeResolver(context: Context) : BaseResolver<FavoriteEpisode>(context) {

    override val contentUri = EPISODE_CONTENT_URI
    override val existsContentUri = EXISTS_EPISODE_CONTENT_URI

    override fun createEntity(id: Int, date: Long) = FavoriteEpisode(id, date)
}