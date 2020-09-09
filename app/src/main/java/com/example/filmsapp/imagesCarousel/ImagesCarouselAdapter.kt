package com.example.filmsapp.imagesCarousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.decode.DataSource
import coil.request.Request
import com.example.filmsapp.base.BaseViewHolder
import com.example.filmsapp.databinding.ItemCarouselImageBinding

class ImagesCarouselAdapter(
    private val onImageReadyListener: () -> Unit
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            ItemCarouselImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onImageReadyListener
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun submitList(images: List<String>) {
        this.images.addAll(images)
        notifyDataSetChanged()
    }
}

class ImageViewHolder(
    private val binding: ItemCarouselImageBinding,
    private val onImageReadyListener: () -> Unit
) : BaseViewHolder(binding.root) {

    override fun bind(model: Any?) {
        (model as? String)?.let {
            binding.url = it
            binding.imageCarousel.transitionName = it
        }
        binding.requestListener = object : Request.Listener {
            override fun onSuccess(request: Request, source: DataSource) {
                onImageReadyListener()
            }

            override fun onError(request: Request, throwable: Throwable) {
                onImageReadyListener()
            }
        }
        binding.executePendingBindings()
    }
}
