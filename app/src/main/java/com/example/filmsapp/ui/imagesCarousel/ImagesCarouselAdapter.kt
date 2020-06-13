package com.example.filmsapp.ui.imagesCarousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.databinding.ItemCarouselImageBinding
import com.example.filmsapp.ui.base.BaseViewHolder

class ImagesCarouselAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(ItemCarouselImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun submitList(images: List<String>) {
        this.images.addAll(images)
    }
}

class ImageViewHolder(val binding: ItemCarouselImageBinding) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {
        (model as? String)?.let {
            binding.url = it
        }
        binding.executePendingBindings()
    }
}