package ru.eugene.rickandmorty.listsscreen.extensions

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.eugene.rickandmorty.R

fun TextView.setColor(color: Int) {
    setTextColor(color)
    visibility = View.VISIBLE
}

fun ImageView.setColor(color: Int) {
    setColorFilter(color)
    visibility = View.VISIBLE
}

fun ImageView.setFavoriteStatus(isFavorite: Boolean) =
    if (isFavorite) {
        setImageResource(R.drawable.favorite_icon)
    } else {
        setImageResource(R.drawable.not_favorite_icon)
    }

fun TextView.setFavoriteStatus(isFavorite: Boolean, favDrawable: Drawable, notFavDrawable: Drawable) =
    if (isFavorite) {
        setCompoundDrawablesWithIntrinsicBounds(favDrawable, null, null, null)
        tag = R.drawable.favorite_icon
    } else {
        setCompoundDrawablesWithIntrinsicBounds(notFavDrawable, null, null, null)
        tag = R.drawable.not_favorite_icon
    }

fun TextView.changeFavoriteStatus(favDrawable: Drawable, notFavDrawable: Drawable) {
    tag = if (tag == R.drawable.not_favorite_icon) {
        setCompoundDrawablesWithIntrinsicBounds(favDrawable, null, null, null)
        R.drawable.favorite_icon
    } else {
        setCompoundDrawablesWithIntrinsicBounds(notFavDrawable, null, null, null)
        R.drawable.not_favorite_icon
    }
}