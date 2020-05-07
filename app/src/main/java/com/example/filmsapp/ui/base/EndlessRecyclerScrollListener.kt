package com.example.filmsapp.ui.base

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerScrollListener(private var layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibleItemCount: Int = layoutManager.childCount + MIN_COUNT_VISIBLE_ITEMS
        val totalItemCount: Int = layoutManager.itemCount
        val lastVisibleItemPosition: Int = getLastItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + lastVisibleItemPosition >= totalItemCount &&
                lastVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    private fun getLastItemPosition() =
        when (layoutManager) {
            is GridLayoutManager -> {
                (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            else -> 0
        }

    companion object {
        const val MIN_COUNT_VISIBLE_ITEMS = 5
        const val PAGE_SIZE = 20
    }
}