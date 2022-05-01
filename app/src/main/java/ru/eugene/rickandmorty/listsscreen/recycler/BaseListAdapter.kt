package ru.eugene.rickandmorty.listsscreen.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.entity.Entity

abstract class BaseListAdapter<E : Entity, VH : BaseListAdapter.BaseViewHolder<E>>(
    diffUtilCallback: DiffUtil.ItemCallback<E>,
    @LayoutRes private val layoutId: Int,
    private val onItemClickAction: (Int) -> Unit,
    private val onFavoriteIconAction: ((Int, Int) -> Unit)?,
    private val onFavoriteClickableObserve: ((LifecycleOwner, Observer<Set<Int>>) -> Unit)?
) : ListAdapter<E, VH>(diffUtilCallback) {

    var isLoading: Boolean = false

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        if (viewType == layoutId) {
            createViewHolder(
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            ).also { holder ->
                holder.itemView.setOnClickListener {
                    if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClickAction(getItem(holder.adapterPosition).id)
                    }
                }
                holder.favoriteImageView?.also { favoriteIcon ->
                    favoriteIcon.setOnClickListener {
                        val position = holder.adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            onFavoriteIconAction?.invoke(getItem(position).id, position)
                        }
                    }
                }
            }
        } else {
            createViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
            )
        }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        if (getItemViewType(position) == layoutId) {
            holder.bind(getItem(position))
            onFavoriteClickableObserve?.invoke(
                holder.itemView.context as LifecycleOwner,
                Observer<Set<Int>> {
                    holder.favoriteImageView?.isClickable = !it.contains(getItem(position).id)
                }
            )
        }
    }

    final override fun getItemViewType(position: Int): Int =
        if (position < super.getItemCount()) {
            layoutId
        } else {
            R.layout.item_loading
        }

    final override fun getItemCount(): Int =
        if (isLoading && super.getItemCount() != 0) {
            super.getItemCount() + 1
        } else {
            super.getItemCount()
        }

    abstract fun createViewHolder(view: View): VH

    abstract class BaseViewHolder<E : Entity>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView = itemView.findViewById<TextView>(R.id.name)

        abstract val favoriteImageView: ImageView?

        @CallSuper
        open fun bind(entity: E) = showNameEntity(entity)

        private fun showNameEntity(entity: E) {
            nameTextView.text = entity.name
        }
    }
}