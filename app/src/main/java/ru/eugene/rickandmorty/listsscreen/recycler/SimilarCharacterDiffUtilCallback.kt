package ru.eugene.rickandmorty.listsscreen.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.eugene.rickandmorty.listsscreen.entity.SimilarCharacter

object SimilarCharacterDiffUtilCallback : DiffUtil.ItemCallback<SimilarCharacter>() {

    override fun areItemsTheSame(oldItem: SimilarCharacter, newItem: SimilarCharacter): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SimilarCharacter, newItem: SimilarCharacter): Boolean =
        oldItem == newItem
}