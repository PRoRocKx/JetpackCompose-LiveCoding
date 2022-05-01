package ru.eugene.rickandmorty.listsscreen.recycler

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.entity.Location
import ru.eugene.rickandmorty.listsscreen.extensions.setFavoriteStatus

class LocationListAdapter(
    @LayoutRes private val layoutId: Int,
    onItemClickAction: (Int) -> Unit,
    onFavoriteIconAction: ((Int, Int) -> Unit)? = null,
    onFavoriteClickableObserve: ((LifecycleOwner, Observer<Set<Int>>) -> Unit)? = null
) : BaseListAdapter<Location, LocationListAdapter.LocationViewHolder>(
    LocationDiffUtilCallback,
    layoutId,
    onItemClickAction,
    onFavoriteIconAction,
    onFavoriteClickableObserve
) {

    override fun createViewHolder(view: View): LocationViewHolder = LocationViewHolder(view)

    class LocationViewHolder(itemView: View) :
        BaseListAdapter.BaseViewHolder<Location>(itemView) {

        override val favoriteImageView: ImageView? by lazy { itemView.findViewById(R.id.favoriteIcon) }

        override fun bind(entity: Location) {
            super.bind(entity)
            favoriteImageView?.setFavoriteStatus(entity.isFavorite)
        }
    }
}