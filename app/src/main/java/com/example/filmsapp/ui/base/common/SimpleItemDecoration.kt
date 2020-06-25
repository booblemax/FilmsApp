package com.example.filmsapp.ui.base.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleItemDecoration(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {

    constructor(all: Int) : this(all, all, all, all)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(left, top, right, bottom)
    }
}
