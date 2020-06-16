package com.example.filmsapp.ui.imagesCarousel

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ImageCarouselFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesCarouselFragment :
    BaseFragment<ImagesCarouselViewModel, ImageCarouselFragmentBinding>() {

    override val viewModel: ImagesCarouselViewModel by viewModel()
    override val layoutRes: Int = R.layout.image_carousel_fragment

    private val carouselAdapter = ImagesCarouselAdapter()

    override fun init() {
        with(binding.imagesCarousel) {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = carouselAdapter
            ImagesCarouselFragmentArgs.fromBundle(requireArguments()).let {
                carouselAdapter.submitList(it.urls.asList())
                scrollToPosition(it.position)
            }
            PagerSnapHelper().apply { attachToRecyclerView(this@with) }
            ItemTouchHelper(
                SwipeToDismissItemTouchCallback { onBackPressed() }).apply {
                    attachToRecyclerView(this@with)
                }
        }
    }

    override fun onBackPressed() {
        findNavController().navigateUp()
    }
}