package com.example.filmsapp.ui.main

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.util.dp

class SimpleItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(0, 32.dp, 0, 32.dp)
    }
}