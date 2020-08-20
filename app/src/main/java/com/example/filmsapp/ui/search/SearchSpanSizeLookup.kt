package com.example.filmsapp.ui.search

import androidx.recyclerview.widget.GridLayoutManager

class SearchSpanSizeLookup(
    private val adapter: SearchAdapter
) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int =
        if (adapter.isLoading && position == adapter.itemCount - 1) {
            2
        } else 1
}
