package com.example.filmsapp.ui.lists

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.invisible
import com.example.filmsapp.util.visible

@BindingAdapter("labelVisibility")
fun labelVisibility(textView: AppCompatTextView, emptyData: Event<Boolean>?) {
    emptyData?.getContentIfNotHandled()?.let {
        if (it) textView.visible() else textView.gone()
    }
}

@BindingAdapter("listVisibility")
fun listVisibility(recyclerView: RecyclerView, emptyData: Event<Boolean>?) {
    emptyData?.getContentIfNotHandled()?.let {
        if (it) recyclerView.visible() else recyclerView.invisible()
    }
}
