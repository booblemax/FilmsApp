package com.example.filmsapp.base.common

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class WrappedGridLayoutManager(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    /**
     * When uses loading item in adapter for recyclerview layout manager has wrong
     * calculations for layout children and throw unneeded exception which crash app. For avoid that
     * exception we handle it here
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Timber.e("IndexOutOfBoundsException occured in RecyclerView: $recycler")
        }
    }
}
