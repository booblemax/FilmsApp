package com.example.filmsapp.ui.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ItemFilmLoadingBinding
import com.example.filmsapp.databinding.ItemListsFilmBinding
import com.example.filmsapp.ui.base.BaseViewHolder
import com.example.filmsapp.ui.base.common.FilmsDiffUtils
import com.example.filmsapp.ui.base.models.FilmModel

class ListsAdapter(
    private val onItemClickListener: (FilmModel) -> Unit
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val items = mutableListOf<FilmModel>()
    private var deepestIndex = DEFAULT_INDEX
    var isLoading = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int =
        if (isLoading && (position == items.size || itemCount == 1)) {
            R.layout.item_film_loading
        } else {
            R.layout.item_lists_film
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.item_lists_film -> {
                val binding = ItemListsFilmBinding.inflate(LayoutInflater.from(parent.context))
                FilmsViewHolder(binding).also { holder ->
                    holder.itemView.setOnClickListener {
                        getItem(holder.adapterPosition)?.let { item -> onItemClickListener(item) }
                    }
                }
            }
            else -> {
                val binding = ItemFilmLoadingBinding.inflate(LayoutInflater.from(parent.context))
                LoadingViewHolder(binding.root)
            }
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        if (holder.adapterPosition > deepestIndex && holder !is LoadingViewHolder) {
            val animation =
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.item_animation_fall_left
                )
            holder.itemView.startAnimation(animation)
            deepestIndex = holder.adapterPosition
        }
    }

    fun submitList(list: List<FilmModel>) {
        val oldList = items.toMutableList()

        items.clear()
        items.addAll(list)

        val callBack = FilmsDiffUtils(oldList, items)
        val result = DiffUtil.calculateDiff(callBack)
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size + isNeedLoadingItem()

    private fun isNeedLoadingItem() = if (isLoading) 1 else 0

    private fun getItem(position: Int): FilmModel? =
        if (position >= 0 && position < items.size) items[position] else null

    companion object {
        private const val DEFAULT_INDEX = -1
    }
}

class FilmsViewHolder(
    private val binding: ItemListsFilmBinding
) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {
        (model as? FilmModel)?.let {
            binding.path = it.poster
            binding.executePendingBindings()
        }
    }
}

class LoadingViewHolder(
    view: View
) : BaseViewHolder(view) {

    override fun bind(model: Any?) {}
}
