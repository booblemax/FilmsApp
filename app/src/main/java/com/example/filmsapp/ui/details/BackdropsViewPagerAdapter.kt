package com.example.filmsapp.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.filmsapp.data.remote.response.BackdropDto
import com.example.filmsapp.databinding.ItemBackdropBinding
import com.example.filmsapp.ui.base.BaseViewHolder

class BackdropsViewPagerAdapter(private val onClickListener: (Int) -> Unit) : ListAdapter<BackdropDto, BackdropViewHolder>(
    object : DiffUtil.ItemCallback<BackdropDto>() {
        override fun areItemsTheSame(oldItem: BackdropDto, newItem: BackdropDto): Boolean =
            oldItem.filePath == newItem.filePath

        override fun areContentsTheSame(oldItem: BackdropDto, newItem: BackdropDto): Boolean =
            oldItem == newItem
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackdropViewHolder {
        return BackdropViewHolder(
            ItemBackdropBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener { onClickListener(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: BackdropViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class BackdropViewHolder(private val binding: ItemBackdropBinding) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {
        binding.url = (model as? BackdropDto)?.filePath
    }
}