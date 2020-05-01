package com.zemingo.drinksmenu.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SpacerItemDecoration(
    private val top: Int = 0,
    private val left: Int = 0,
    private val bottom: Int = 0,
    private val right: Int = 0
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter
        adapter ?: return
        if (parent.getChildAdapterPosition(view) != adapter.itemCount - 1) {
            outRect.top = top
            outRect.left = left
            outRect.bottom = bottom
            outRect.right = right
        }
    }
}