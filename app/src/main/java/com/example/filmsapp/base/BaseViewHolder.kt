package com.example.filmsapp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    abstract fun bind(model: Any?)
}
