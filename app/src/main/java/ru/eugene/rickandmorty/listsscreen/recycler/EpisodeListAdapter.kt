package ru.eugene.rickandmorty.listsscreen.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.extensions.setFavoriteStatus

class EpisodeListAdapter(
    @LayoutRes private val layoutId: Int,
    onItemClickAction: (Int) -> Unit,
    onFavoriteIconAction: ((Int, Int) -> Unit)? = null,
    onFavoriteClickableObserve: ((LifecycleOwner, Observer<Set<Int>>) -> Unit)? = null
) : BaseListAdapter<Episode, EpisodeListAdapter.EpisodeViewHolder>(
    EpisodeDiffUtilCallback,
    layoutId,
    onItemClickAction,
    onFavoriteIconAction,
    onFavoriteClickableObserve
) {

    override fun createViewHolder(view: View): EpisodeViewHolder = EpisodeViewHolder(view)

    class EpisodeViewHolder(itemView: View) :
        BaseListAdapter.BaseViewHolder<Episode>(itemView) {

        private val numberEpisodeTextView = itemView.findViewById<TextView>(R.id.numberEpisode)
        override val favoriteImageView: ImageView? by lazy { itemView.findViewById(R.id.favoriteIcon) }

        override fun bind(entity: Episode) {
            super.bind(entity)
            favoriteImageView?.setFavoriteStatus(entity.isFavorite)
            numberEpisodeTextView.text =
                if (entity.numberOfEpisodeInfo.season != null) {
                    itemView.context.getString(
                        R.string.numberEpisode,
                        entity.numberOfEpisodeInfo.season,
                        entity.numberOfEpisodeInfo.episode
                    )
                } else {
                    entity.numberOfEpisodeInfo.codeOfEpisode
                }
        }
    }
}