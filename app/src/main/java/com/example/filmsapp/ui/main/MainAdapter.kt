package com.example.filmsapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ItemFilmBinding
import com.example.filmsapp.databinding.ItemLoadingBinding
import com.example.filmsapp.ui.base.BaseViewHolder
import com.example.filmsapp.ui.base.models.FilmModel

class MainAdapter(
    private val onItemClickListener: (FilmModel) -> Unit
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val items = mutableListOf<FilmModel>()
    private var deepestIndex = DEFAULT_INDEX
    var isLoading = false

    override fun getItemViewType(position: Int): Int =
        if (isLoading && position == items.size) {
            R.layout.item_loading
        } else {
            R.layout.item_film
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.item_film -> {
                val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context))
                MainViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context))
                LoadingViewHolder(binding)
            }
        }.also { holder ->
            holder.itemView.setOnClickListener {
                getItem(holder.adapterPosition)?.let { item -> onItemClickListener(item) }
            }
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        if (holder.adapterPosition > deepestIndex) {
            val animation =
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.item_animation_fall_down
                )
            holder.itemView.startAnimation(animation)
            deepestIndex = holder.adapterPosition
        }
    }

    fun submitList(list: MutableList<FilmModel>, isReload: Boolean = false) {
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

class MainViewHolder(
    private val binding: ItemFilmBinding
) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {
        (model as? FilmModel)?.let {
            binding.model = it
            binding.executePendingBindings()
        }
    }
}

class LoadingViewHolder(
    binding: ItemLoadingBinding
) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {}
}

private class FilmsDiffUtils(
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
