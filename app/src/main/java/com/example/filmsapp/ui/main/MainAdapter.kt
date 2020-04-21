package com.example.filmsapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ItemFilmBinding
import com.example.filmsapp.ui.base.models.FilmModel

class MainAdapter : ListAdapter<FilmModel, MainViewHolder>(
    object : DiffUtil.ItemCallback<FilmModel>() {
        override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean =
            oldItem == newItem
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = DataBindingUtil.inflate<ItemFilmBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_film,
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MainViewHolder(
    private val binding: ItemFilmBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(filmModel: FilmModel) {
        binding.model = filmModel
        binding.executePendingBindings()
    }

}