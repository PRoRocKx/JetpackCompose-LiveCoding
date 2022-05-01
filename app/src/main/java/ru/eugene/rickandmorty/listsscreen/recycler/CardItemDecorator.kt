package ru.eugene.rickandmorty.listsscreen.recycler

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class CardItemDecorator(@Px private val offsetPx: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(rect: Rect, view: View, parent: RecyclerView, s: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION && position > 0) {
            rect.set(offsetPx, 0, 0, 0)
        }
    }
}