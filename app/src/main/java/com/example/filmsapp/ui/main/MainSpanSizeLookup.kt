package com.example.filmsapp.ui.main

import androidx.recyclerview.widget.GridLayoutManager

class MainSpanSizeLookup(
    private val adapter: MainAdapter
) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int =
        if (adapter.isLoading && position == adapter.itemCount - 1) {
            2
        } else 1
}
