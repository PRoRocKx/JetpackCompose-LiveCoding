package ru.eugene.rickandmorty.listsscreen.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.entity.AliveStatus
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.extensions.getColorById
import ru.eugene.rickandmorty.listsscreen.extensions.setColor
import ru.eugene.rickandmorty.listsscreen.extensions.setFavoriteStatus

class CharacterListAdapter(
    @LayoutRes private val layoutId: Int,
    onItemClickAction: (Int) -> Unit,
    onFavoriteIconAction: ((Int, Int) -> Unit)? = null,
    onFavoriteClickableObserve: ((LifecycleOwner, Observer<Set<Int>>) -> Unit)? = null
) : BaseListAdapter<Character, CharacterListAdapter.CharacterViewHolder>(
    CharacterDiffUtilCallback,
    layoutId,
    onItemClickAction,
    onFavoriteIconAction,
    onFavoriteClickableObserve
) {

    override fun createViewHolder(view: View): CharacterViewHolder = CharacterViewHolder(view)

    class CharacterViewHolder(itemView: View) :
        BaseListAdapter.BaseViewHolder<Character>(itemView) {

        private val statusTextView = itemView.findViewById<TextView>(R.id.statusDescription)
        private val pointImageView = itemView.findViewById<ImageView>(R.id.statusPoint)
        private val photoImageView = itemView.findViewById<ImageView>(R.id.imageCharacter)
        override val favoriteImageView: ImageView? by lazy { itemView.findViewById(R.id.favoriteIcon) }

        override fun bind(entity: Character) {
            super.bind(entity)
            favoriteImageView?.setFavoriteStatus(entity.isFavorite)
            Glide.with(itemView.context)
                .load(entity.image)
                .into(photoImageView)
            showStatus(entity)
        }

        private fun showStatus(character: Character) {
            when (character.status) {
                AliveStatus.UNKNOWN -> {
                    statusTextView.visibility = View.INVISIBLE
                    pointImageView.visibility = View.INVISIBLE
                }
                else -> {
                    itemView.context?.getColorById(character.status.color)?.let {
                        setStatus(it, character.status.description)
                    }
                }
            }
        }

        private fun setStatus(color: Int, statusDescription: String) {
            statusTextView.text = statusDescription
            statusTextView.setColor(color)
            pointImageView.setColor(color)
        }
    }
}