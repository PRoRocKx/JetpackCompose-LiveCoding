package ru.eugene.rickandmorty.listsscreen.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.eugene.rickandmorty.listsscreen.entity.Location

object LocationDiffUtilCallback : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Location, newItem: Location) = Unit
}