package com.example.filmsapp.lists

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.base.Event
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.invisible
import com.example.filmsapp.util.visible

@BindingAdapter("labelVisibility", "emptyQuery", requireAll = false)
fun labelVisibility(
    textView: AppCompatTextView,
    emptyData: Event<Boolean>? = Event(false),
    emptyQuery: Event<Boolean>? = Event(false)
) {
    emptyData?.getContentIfNotHandled()?.let {
        if (it) textView.visible() else textView.gone()
    }
    emptyQuery?.getContentIfNotHandled()?.let {
        textView.text = if (it) {
            textView.context.getString(R.string.empty_data)
        } else {
            textView.context.getString(R.string.label_input_film_name)
        }
    }
}

@BindingAdapter("listVisibility")
fun listVisibility(recyclerView: RecyclerView, emptyData: Event<Boolean>?) {
    emptyData?.getContentIfNotHandled()?.let {
        if (it) recyclerView.visible() else recyclerView.invisible()
    }
}
