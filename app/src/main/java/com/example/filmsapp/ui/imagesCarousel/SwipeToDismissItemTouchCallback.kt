package com.example.filmsapp.ui.imagesCarousel

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDismissItemTouchCallback(private val onSwipeCallback: () -> Unit) :
    ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.UP or ItemTouchHelper.DOWN
    ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeCallback()
    }
}
