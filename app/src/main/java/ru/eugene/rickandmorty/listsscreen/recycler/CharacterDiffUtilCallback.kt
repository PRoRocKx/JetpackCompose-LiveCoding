package ru.eugene.rickandmorty.listsscreen.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.eugene.rickandmorty.listsscreen.entity.Character

object CharacterDiffUtilCallback : DiffUtil.ItemCallback<Character>() {

    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Character, newItem: Character) = Unit
}