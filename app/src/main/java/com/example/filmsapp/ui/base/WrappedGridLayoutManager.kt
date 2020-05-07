package com.example.filmsapp.ui.base

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.lang.IndexOutOfBoundsException

class WrappedGridLayoutManager(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
    ) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Timber.e("IndexOutOfBoundsException occured in RecyclerView")
        }
    }
}