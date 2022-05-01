package ru.eugene.rickandmorty.listsscreen.recycler

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(private val dividerDrawable: Drawable, @Px private val startOffsetPx: Int) :
    RecyclerView.ItemDecoration() {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        repeat(parent.childCount - 1) {
            val top = parent.getChildAt(it).bottom
            val bottom = top + dividerDrawable.intrinsicHeight
            dividerDrawable.setBounds(startOffsetPx, top, parent.width, bottom)
            dividerDrawable.draw(canvas)
        }
    }
}