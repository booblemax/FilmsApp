package com.example.filmsapp.ui.base.common

import androidx.recyclerview.widget.DiffUtil
import com.example.filmsapp.ui.base.models.FilmModel

class FilmsDiffUtils(
    private val oldList: List<FilmModel>,
    private val newList: List<FilmModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
