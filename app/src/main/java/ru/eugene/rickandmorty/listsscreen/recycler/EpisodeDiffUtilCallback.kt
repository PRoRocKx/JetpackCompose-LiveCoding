package ru.eugene.rickandmorty.listsscreen.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.eugene.rickandmorty.listsscreen.entity.Episode

object EpisodeDiffUtilCallback : DiffUtil.ItemCallback<Episode>() {

    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Episode, newItem: Episode) = Unit
}