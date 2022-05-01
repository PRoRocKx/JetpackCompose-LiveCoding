package ru.eugene.rickandmorty.listsscreen.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorById(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getDrawableById(resourceId: Int): Drawable? =
    ContextCompat.getDrawable(this, resourceId)