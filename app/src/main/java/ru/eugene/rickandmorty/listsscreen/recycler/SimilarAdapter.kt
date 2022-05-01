package ru.eugene.rickandmorty.listsscreen.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.entity.SimilarCharacter

class SimilarAdapter(
    private val onItemClickAction: (Int) -> Unit
) : ListAdapter<SimilarCharacter, SimilarAdapter.SimilarViewHolder>(
    SimilarCharacterDiffUtilCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder =
        SimilarViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.similar_character_card, parent, false)
        )

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class SimilarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.characterImage)
        private val nameTextView = itemView.findViewById<TextView>(R.id.name)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClickAction(getItem(adapterPosition).id)
                }
            }
        }

        fun bind(similarCharacter: SimilarCharacter) {
            Glide.with(itemView.context)
                .load(similarCharacter.image)
                .into(imageView)
            nameTextView.text = similarCharacter.name
        }
    }
}