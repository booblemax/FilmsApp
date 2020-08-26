package com.example.filmsapp.base.common

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class WrappedLinearLayoutManager(
    context: Context?,
    orientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    /**
     * When uses loading item in adapter for recyclerview layout manager has wrong
     * calculations for layout children and throw unneeded exception which crash app. For avoid that
     * exception we handle it here
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Timber.e("IndexOutOfBoundException occur in RecyclerView: $recycler")
        }
    }
}
